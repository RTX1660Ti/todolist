package org.example.common.enums;

/**
 *@className DateFormatUtils
 *@author pmg
 *@date 2021/4/21
 *@description
 **/
public enum ResponseCode {
    /**
     * 未知错误
     */
    UNKNOWN(-1, "未知错误"),
    /**
     * 请求成功
     */
    SUCCESS(20000, "成功"),
    /**
     * 请求失败
     */
    FAILURE(20002, "失败"),

    // ------------------------------------------------------- 参数错误：10001-19999 Start

    /**
     * 参数无效
     */
    PARAM_IS_INVALID(10001, "参数无效"),
    /**
     * 参数为空
     */
    PARAM_IS_BLANK(10002, "参数为空"),
    /**
     * 参数类型错误
     */
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    /**
     * 参数缺失
     */
    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    // ------------------------------------------------------- 参数错误：10001-19999 End

    // ------------------------------------------------------- 用户错误：20001-29999 Start

    /**
     * 用户未登录
     */
    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    /**
     * 账号不存在或密码错误
     */
    USER_LOGIN_ERROR(20002, "账号不存在或密码错误"),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST(20004, "用户不存在"),


    // ------------------------------------------------------- 用户错误：20001-29999 End


    // ------------------------------------------------------- 系统错误：30001-39999 Start

    /**
     * 系统繁忙，请稍后重试
     */
    SYSTEM_INNER_ERROR(30001, "系统繁忙，请稍后重试"),

    // ------------------------------------------------------- 系统错误：30001-39999 End

    // ------------------------------------------------------- 数据错误：40001-49999 Start

    /**
     * 数据未找到
     */
    RESULT_DATA_NONE(40001, "数据未找到"),
    /**
     * 数据有误
     */
    DATA_IS_WRONG(40002, "数据有误"),
    /**
     * 数据已存在
     */
    DATA_ALREADY_EXISTED(40003, "数据已存在"),



    // ------------------------------------------------------- 数据错误：40001-49999 End

    // ------------------------------------------------------- 接口错误：50001-59999 Start

    /**
     * 该接口禁止访问
     */
    INTERFACE_FORBID_VISIT(50003, "该接口禁止访问"),
    /**
     * 接口地址无效
     */
    INTERFACE_ADDRESS_INVALID(50004, "接口地址无效"),
    /**
     * 接口请求超时
     */
    INTERFACE_REQUEST_TIMEOUT(50005, "接口请求超时"),


    // ------------------------------------------------------- 接口错误：50001-59999 End

    // ------------------------------------------------------- 权限错误：60001-69999 Start

    /**
     * 无权限
     */
    PERMISSION_NO_ACCESS(60001, "无权限"),

    TOKEN_EXPIRATION(60002,"TOKEN 已失效");

    // ------------------------------------------------------- 权限错误：60001-69999 End

    private Integer code;

    private String message;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResponseCode item : ResponseCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResponseCode item : ResponseCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }

}
