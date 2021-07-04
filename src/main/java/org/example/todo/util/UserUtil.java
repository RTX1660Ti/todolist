package org.example.todo.util;

import org.example.common.enums.ResponseCode;
import org.example.common.exception.BusinessException;
import org.example.todo.bean.constans.CommonConstant;
import org.example.todo.bean.dto.UserDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class UserUtil {

    @Resource
    private HttpServletRequest request;

    @Resource
    private RedisUtil redisUtil;

    public UserDto getUser() {
        return redisUtil.get(token(), UserDto.class);
    }

    public boolean isTokenAuth() {
        return token() != null;
    }

    private String token() {
        String token = request.getHeader(CommonConstant.TOKEN);
        return token == null ? request.getParameter(CommonConstant.TOKEN) : token;
    }

    /**
     *  校验该userId是否有权限
     */
    public Boolean authentication(Long userId){
        if (isTokenAuth()){
            UserDto userDto = getUser();
            if (null != userDto)
                return userDto.getIsAdmin() || userDto.getId().equals(userId);
        }
        return false;
    }


    /**
     *  校验该userId是否是admin
     */
    public Boolean isAdmin(){
        if (isTokenAuth()){
            UserDto userDto = getUser();
            if (null != userDto)
                return userDto.getIsAdmin();
        }
        return false;
    }

    /**
     *  获取userID
     */
    public Long getUserId() {
        UserDto userDto = getUser();
        if (null == userDto)
            throw new BusinessException(ResponseCode.TOKEN_EXPIRATION);
        return userDto.getId();
    }

}
