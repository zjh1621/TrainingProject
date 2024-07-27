package com.example.moon.DTO;

import com.example.moon.exception.CustomizeErrorCode;
import com.example.moon.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResultDTO errorOf(Integer code,String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode customizeErrorCode) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.code=customizeErrorCode.getCode();
        resultDTO.message=customizeErrorCode.getMessage();
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException e) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.code=e.getCode();
        resultDTO.message=e.getMessage();
        return resultDTO;
    }

    public static <T> ResultDTO okOf(T data) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(CustomizeErrorCode.SUCCESS.getCode());
        resultDTO.setMessage(CustomizeErrorCode.SUCCESS.getMessage());
        resultDTO.setData(data);
        return resultDTO;
    }
}
