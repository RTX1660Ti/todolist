package org.example.todo.bean.form;

import lombok.Data;
import org.example.common.PageInfo;

import java.io.Serializable;


@Data
public class BusinessQueryForm extends PageInfo implements Serializable {

    private static final long serialVersionUID = -1991913359756457755L;

    private Long userId;

    //是否已完成
    private Boolean isEnd;

    //是否被删除
    private Boolean isDelete;

    //查询范围,起始时间
    private Long businessStartTime;

    //截止时间
    private Long businessEndTime;

}
