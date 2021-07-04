package org.example.todo.bean.dto;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class UserDto {

    private Long id;

    private String userName;


    private Boolean isAdmin;


    private Boolean inUse;


    private Long createTime;


    private Long updateTime;

}
