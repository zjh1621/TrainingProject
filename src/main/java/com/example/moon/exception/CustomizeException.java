package com.example.moon.exception;


//自定义异常
public class CustomizeException extends RuntimeException{

    private Integer code;
    private String message;


    public CustomizeException(CustomizeErrorCode errorCode) {
        this.code=errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
