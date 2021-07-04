package org.example.common;

import lombok.Data;
import org.example.todo.bean.dto.ResponseDto;

import java.util.HashMap;
import java.util.Map;

@Data
public class CommonReturnData {
    private static final int SUCCESS_CODE = 0;
    private static final int FAIL_CODE = 400;

    private Integer code;
    private String message;
    private Map<String, Object> data;
    public CommonReturnData putData(String key, Object value) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(key, value);
        return this;
    }

    public static CommonReturnData errorRet(int errCode, String errMsg) {
        CommonReturnData ret = new CommonReturnData();
        ret.setCode(errCode);
        ret.setMessage(errMsg);
        return ret;
    }

    public static CommonReturnData errorRet(String errMsg) {
        return errorRet(FAIL_CODE, errMsg);
    }

    public static CommonReturnData successRet() {
        CommonReturnData ret = new CommonReturnData();
        ret.setCode(SUCCESS_CODE);
        ret.setMessage("执行成功");
        return ret;
    }

    public static CommonReturnData successRet(String message) {
        CommonReturnData ret = new CommonReturnData();
        ret.setCode(SUCCESS_CODE);
        ret.setMessage(message);
        return ret;
    }
    public static CommonReturnData resultRet(int code,String message) {
        CommonReturnData ret = new CommonReturnData();
        ret.setCode(code);
        ret.setMessage(message);
        return ret;
    }

    public static CommonReturnData resultRet(ResponseDto responseDto) {
        CommonReturnData ret = new CommonReturnData();
        ret.setCode(responseDto.getResultCode());
        ret.setMessage(responseDto.getResultMsg());
        if (null != responseDto.getData() && null != responseDto.getData().get("data"))
            ret.putData("data",responseDto.getData().get("data"));
        return ret;
    }

    public static CommonReturnData successRet(String message, Map<String, Object> data) {
        CommonReturnData ret = new CommonReturnData();
        ret.setCode(SUCCESS_CODE);
        ret.setMessage(message);
        ret.setData(data);
        return ret;
    }
}
