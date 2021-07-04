package org.example.todo.domain.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long userId;

    @Column
    private String description;

    @Column
    private Boolean isEnd;

    @Column
    private Boolean isDelete;

    @Column
    private Long businessTime;

    @Column
    private Long createTime;

    @Column
    private Long updateTime;
}
