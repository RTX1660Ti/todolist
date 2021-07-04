package org.example.todo.bean.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class BusinessUpForm implements Serializable {

    private static final long serialVersionUID = -890626770541546183L;

    @NotNull(message = "事务Id不能为空")
    private Long id;

    private Long userId;

    private String description;

    private Long businessTime;

    private Boolean isEnd;

    private Boolean isDelete;

}
