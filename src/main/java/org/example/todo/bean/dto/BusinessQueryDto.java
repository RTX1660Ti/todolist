package org.example.todo.bean.dto;

import lombok.Data;
import org.example.common.PageInfo;

import java.io.Serializable;


@Data
public class BusinessQueryDto extends PageInfo implements Serializable {

    private static final long serialVersionUID = 6299704073955377129L;

    private Long userId;

    private Boolean isEnd;

    private Boolean isDelete;

    private Long businessStartTime;

    private Long businessEndTime;

}
