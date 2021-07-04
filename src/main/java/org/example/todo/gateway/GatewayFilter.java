package org.example.todo.gateway;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.todo.bean.constans.CommonConstant;
import org.example.todo.bean.dto.UserDto;
import org.example.todo.domain.entity.User;
import org.example.todo.gateway.conf.GatewayProperties;
import org.example.todo.util.RedisUtil;
import org.example.todo.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.example.common.enums.ResponseCode.PERMISSION_NO_ACCESS;
import static org.example.todo.bean.constans.CommonConstant.TOKEN;


/**
 * 这里只实现基本的过滤,鉴权功能,只用个基础的Filter足够用了
 *
 */
@Component
@Slf4j
public class GatewayFilter implements Filter {

    @Resource
    private GatewayProperties gatewayProperties;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserUtil userUtil;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
        StopWatch stopWatch = new StopWatch(Long.toString(Thread.currentThread().getId()) + System.currentTimeMillis());
        stopWatch.start();
        // 处理请求
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.info("accept request:" + httpRequest.getRequestURL() + "|method:" + httpRequest.getMethod());
        log.info("request param:" + JSONObject.toJSONString(httpRequest.getParameterMap()));
        //是否是免验证url
        String nowPath = ((HttpServletRequest) request).getServletPath();
        if (gatewayProperties.getLoginAllowPaths().contains(nowPath)) {
            log.info("accept request:{}|{}是免验证url，跳过验证",httpRequest.getRequestURL(),httpRequest.getMethod());
            filter.doFilter(request, response);
            stopWatch.stop();
            log.info("this request[{}]cast time:{}ms",httpRequest.getRequestURI(),(stopWatch.getTotalTimeMillis()));
            return;
        }
        //验证token,校验身份信息
        String token = httpRequest.getHeader(CommonConstant.HEADER_TOKEN);
        out:
        if (StringUtils.isNotBlank(token)){
            UserDto user = userUtil.getUser();
            if (null == user)
                break out;
            String trueToken = (String)redisUtil.get(TOKEN+user.getUserName());
            //token存在且一致,放行
            if (StringUtils.isNotBlank(trueToken) && trueToken.equals(token)){
                filter.doFilter(request, response);
                stopWatch.stop();
                log.info("this request[{}]cast time:{}ms",httpRequest.getRequestURI(),(stopWatch.getTotalTimeMillis()));
            }
        }
        //校验没过,返回错误
        {
            log.error("request {}|method:{},authorization validtion failed",httpRequest.getRequestURI(),JSONObject.toJSONString(httpRequest.getParameterMap()));
            // 验证不通过，返回异常信息
            ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            String respString = JSONObject.toJSONString(PERMISSION_NO_ACCESS);
            PrintWriter out = response.getWriter();
            out.print(respString);
            out.close();
        }
    }

    @Override
    public void destroy() {
    }


}

