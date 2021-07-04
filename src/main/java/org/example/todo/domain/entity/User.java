package org.example.todo.domain.entity;


import lombok.Data;

import javax.persistence.*;


/**
 *  用户实体类
 *
 *  注：这边只搞最基础的功能，无单独的权限表、角色表等
 *
 */
@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String userName;

    @Column
    private String passWord;

    /**
     *  是否是管理员
     */
    @Column
    private Boolean isAdmin;


    /**
     *  账号是否在用
     */
    @Column
    private Boolean inUse;


    @Column
    private Long createTime;


    @Column
    private Long updateTime;


}
