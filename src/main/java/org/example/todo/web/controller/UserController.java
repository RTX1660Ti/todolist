package org.example.todo.web.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.common.CommonReturnData;
import org.example.common.exception.BusinessException;
import org.example.todo.bean.dto.ResponseDto;
import org.example.todo.bean.dto.UserDto;
import org.example.todo.bean.form.UserLoginForm;
import org.example.todo.domain.service.UserService;
import org.example.todo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.net.BindException;

import static org.example.todo.bean.constans.CommonConstant.CAPTCHA_CODE;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 获取验证码
     * @param captchaKey  此为验证码对应的key，登录时不仅需要提供验证码，还需要提供验证码对应的key
     */
    @GetMapping("/code/img")
    public void generateCod(@RequestParam String captchaKey, HttpServletResponse response) throws BindException {
        //设置response响应
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        try (ServletOutputStream out = response.getOutputStream())
        {
            //生成验证码
            CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(100, 38, 4, 5);
            //保存验证码
            redisUtil.set(CAPTCHA_CODE+captchaKey,captcha.getCode(),60);
            captcha.write(out);
        }catch (Exception e){
            log.error("验证码生成异常:"+e.getMessage(),e);
            throw new BindException("验证码获取失败");
        }
    }

    /**
     *  登录
     *  1、需要验证验证码
     *  2、登录失败超5次就锁定
     *  3、登录时不仅需要提供验证码，还需要提供验证码对应的key
     */
    @PostMapping(value = "/login",produces =  "application/json;charset=UTF-8")
    public CommonReturnData login(@Valid @RequestBody UserLoginForm loginForm) throws BusinessException {
        CommonReturnData returnData ;
        try{
            //验证码校验
            validCacheCode(loginForm);
            ResponseDto responseDto = userService.login(loginForm);
            {
                returnData = CommonReturnData.resultRet(responseDto.getResultCode(),responseDto.getResultMsg());
                returnData.putData("token",StringUtils.isNotBlank(responseDto.getToken())?responseDto.getToken():"");
            }
        }catch (Exception e){
            log.error("【"+ loginForm.getUserName()+"】登录异常:"+e.getMessage(),e);
            throw new BusinessException("登录失败:"+e.getMessage());
        }
        return returnData;
    }

    /**
     *  登出
     */
    @PostMapping(value = "/loginOut",produces =  "application/json;charset=UTF-8")
    public CommonReturnData loginOut(@Valid @RequestBody LogoutForm logoutForm) {
        return CommonReturnData.resultRet(userService.loginOut(logoutForm.getUserName(),logoutForm.getToken()));
    }




    //=============================一次性===================================

    @Data
    static class LogoutForm{
        @NotEmpty(message = "userName不能为空!")
        private String userName;

        @NotEmpty(message = "token不能为空!")
        private String token;
    }

    //=============================私有方法===================================

    private void validCacheCode(UserLoginForm loginForm){
        Object cacheCode = redisUtil.get(CAPTCHA_CODE + loginForm.getCaptchaKey());
        {
            if (redisUtil.exists(CAPTCHA_CODE + loginForm.getCaptchaKey())){
                redisUtil.del(CAPTCHA_CODE + loginForm.getCaptchaKey());
            }
            if (null == cacheCode || StringUtils.isEmpty(cacheCode.toString())
                    || StringUtils.isEmpty(loginForm.getValidCode()) || !cacheCode.equals(loginForm.getValidCode())) {
                throw new BusinessException("验证码错误或已失效!");
            }
        }
    }



}
