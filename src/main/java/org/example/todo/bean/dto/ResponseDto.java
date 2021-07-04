package org.example.todo.bean.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.example.common.enums.ResponseCode;

import java.io.Serializable;
import java.util.Map;

@Data
@ToString
@Accessors(chain = true)
public class ResponseDto implements Serializable {
    private static final long serialVersionUID = -5322049538778765829L;

    public ResponseDto(){
    }
    public ResponseDto(ResponseCode responseCode){
        this.resultCode = responseCode.code();
        this.resultMsg = responseCode.message();
    }
    private Integer resultCode;
    private String resultMsg;
    private String token;
    private Map<String,Object> data;
}
