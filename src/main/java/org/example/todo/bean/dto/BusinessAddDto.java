package org.example.todo.bean.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class BusinessAddDto  implements Serializable {

    private static final long serialVersionUID = 2019575291885759587L;

    private Long userId;

    private String description;

    private Long businessTime;

}
