package org.example.todo.domain.dao;

import org.example.todo.domain.entity.User;

public interface UserMapper {

    User selectById(Long id);

    User selectByName(String userName);

}
