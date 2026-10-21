package com.example.library.common;

import lombok.Getter;

/**
 * 业务异常：由业务规则主动抛出，携带业务错误码
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String message) {
        this(400, message);
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
