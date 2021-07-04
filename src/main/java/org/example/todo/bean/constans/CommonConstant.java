package org.example.todo.bean.constans;


public interface CommonConstant {

    /**
     * redis内，用户登录后方token 的前缀
     */
    public static final String TOKEN = "TOKEN_";

    /**
     * 请求url 的header内,token的前缀
     */
    public static final String HEADER_TOKEN = "token";

    public static final String HEADER_IDENTITY = "Identity";


    /**
     * redis内，验证码的前缀
     */
    public static final String CAPTCHA_CODE = "CAPTCHA_CODE";


    /**
     * redis内，用户登录失败次数的前缀
     */
    public static final String LOGIN_FAIL = "LOGIN_FAIL";


}
