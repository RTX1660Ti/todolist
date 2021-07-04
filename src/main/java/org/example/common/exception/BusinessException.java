package org.example.common.exception;

import org.example.common.enums.ResponseCode;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 8195203362613461975L;

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BusinessException() {}

    public BusinessException(String message) {
        super(message);
        this.code = -1;
    }

    public BusinessException(ResponseCode status) {
        super(status.message());
        this.code = status.code();
    }
}
