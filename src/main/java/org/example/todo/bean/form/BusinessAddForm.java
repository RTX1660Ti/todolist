package org.example.todo.bean.form;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class BusinessAddForm implements Serializable {

    private static final long serialVersionUID = -998241627437081646L;

    private Long userId;

    @NotBlank(message = "事务描述不能为空")
    private String description;

    @NotNull(message = "事务时间不能为空")
    private Long businessTime;
}
