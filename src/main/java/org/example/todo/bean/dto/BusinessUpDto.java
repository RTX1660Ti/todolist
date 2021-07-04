package org.example.todo.bean.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BusinessUpDto implements Serializable {

    private static final long serialVersionUID = 167488777644484802L;

    private Long id;

    private Long userId;

    private String description;

    private Long businessTime;

    private Boolean isEnd;

    private Boolean isDelete;
}
