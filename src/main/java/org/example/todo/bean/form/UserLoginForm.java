package org.example.todo.bean.form;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
public class UserLoginForm implements Serializable {

    private static final long serialVersionUID = -1366359536895619113L;

    @NotBlank(message = "账号不能为空!")
    private String userName;
    @NotBlank(message = "密码不能为空!")
    private String passWord;
    @NotBlank(message = "请输入验证码!")
    private String validCode;

    //登录时不仅需要提供验证码，还需要提供验证码对应的key
    private String captchaKey;

}
