package org.example.todo.domain.service.Impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.common.enums.ResponseCode;
import org.example.common.exception.BusinessException;
import org.example.todo.bean.dto.ResponseDto;
import org.example.todo.bean.dto.UserDto;
import org.example.todo.bean.form.UserLoginForm;
import org.example.todo.domain.dao.UserMapper;
import org.example.todo.domain.entity.User;
import org.example.todo.domain.service.UserService;
import org.example.todo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.example.todo.bean.constans.CommonConstant.LOGIN_FAIL;
import static org.example.todo.bean.constans.CommonConstant.TOKEN;


@Service("UserService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     *  多次登录失败后的锁定时间
     */
    @Value("${user.AccountLockTime}")
    public Long userAccountLockTime;


    /**
     *  登录最大失败次数
     */
    @Value("${user.maxFailures}")
    public Integer maxFailures;

    /**
     *  token有效时间
     */
    @Value("${user.tokenTimes}")
    public Long tokenTimes;

    @Override
    public UserDto selectById(Long id) {
        UserDto userDto = new UserDto();
        User user = userMapper.selectById(id);
        {
            userDto.setId(user.getId());
            userDto.setUserName(user.getUserName());
            userDto.setIsAdmin(user.getIsAdmin());
            userDto.setInUse(user.getInUse());
            userDto.setCreateTime(user.getCreateTime());
            userDto.setUpdateTime(user.getUpdateTime());
        }
        return userDto;
    }

    @Override
    public ResponseDto login(UserLoginForm loginForm) {
        ResponseDto responseDto ;
        //本次是否失败,默认失败
        boolean isFail = true;
        //获取登录失败次数,并校验
        int fail =getLoginFailTimes(loginForm);
        User user = userMapper.selectByName(loginForm.getUserName());
        { //进行校验，并设置返回值
            out:
            if (null != user ){
                if (user.getPassWord().equals(loginForm.getPassWord()) && user.getInUse() == Boolean.TRUE ){
                    isFail = false;
                    responseDto = new ResponseDto(ResponseCode.SUCCESS);
                    responseDto.setToken(getToken(loginForm.getUserName()));
                    //这里的user里的密码是加密过的(放进数据库前就加密了),放进token不会泄密
                    //但是这次方便点,密码在任何地方都是明文,没加密
                    redisUtil.set(responseDto.getToken(), JSONObject.toJSONString(user),tokenTimes);
                    redisUtil.del(LOGIN_FAIL + loginForm.getUserName());
                    break out;
                }
                responseDto = new ResponseDto(ResponseCode.USER_LOGIN_ERROR);
            }else {
                responseDto = new ResponseDto(ResponseCode.USER_NOT_EXIST);
            }
        }
        //密码校验未通过，增加失败次数
        if (isFail){
            fail = fail+1;
            redisUtil.set(LOGIN_FAIL + loginForm.getUserName(),fail,userAccountLockTime);
        }
        return responseDto;
    }

    /**
     *  登出
     *      传入的用户名、token，与redis里正确的数据对应不上，才返回错误，
     *  其余情况均返回成功。
     *
     */
    @Override
    public ResponseDto loginOut(String userName, String token) {
        String trueToken = (String)redisUtil.get(LOGIN_FAIL + userName);
        if (StringUtils.isNotBlank(trueToken) && !trueToken.equals(token)){
            return new ResponseDto(ResponseCode.FAILURE);
        }
        return new ResponseDto(ResponseCode.SUCCESS);
    }


    //=============================私有方法===================================

    private int getLoginFailTimes(UserLoginForm loginForm){
        Object failures = redisUtil.get(LOGIN_FAIL + loginForm.getUserName());
        int fail = null == failures?0:(int)failures;
        if (fail >= maxFailures) {
            log.info("===user:[{}]登录失败次数:{}",loginForm.getUserName(),fail);
            throw new BusinessException("登录失败次数超限，请稍后重试!");
        }
        return fail;
    }

    /**
     *  获取token
     * 1、token仍有效，就延长token时间
     * 2、无效就整个新的
     */
    private String getToken(String userName){
        String token = (String)redisUtil.get(TOKEN+userName);
        if (StringUtils.isNotBlank(token)){
            redisUtil.set(TOKEN + userName,token,tokenTimes);
        }else {
            token = UUID.randomUUID().toString().replaceAll("-","");
        }
        return token;
    }
}
