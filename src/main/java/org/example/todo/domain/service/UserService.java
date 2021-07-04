package org.example.todo.domain.service;

import org.example.todo.bean.dto.ResponseDto;
import org.example.todo.bean.dto.UserDto;
import org.example.todo.bean.form.UserLoginForm;

public interface UserService {

    UserDto selectById(Long id);

    ResponseDto login(UserLoginForm loginForm);

    ResponseDto loginOut(String userName,String token);

}
