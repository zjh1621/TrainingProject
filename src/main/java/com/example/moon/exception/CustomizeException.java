package com.example.moon.exception;


//自定义异常
public class CustomizeException extends RuntimeException{
    private String message;

    public CustomizeException(CustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    public CustomizeException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
