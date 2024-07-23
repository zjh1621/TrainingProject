package com.example.moon.exception;


//自定义错误码
public enum CustomizeErrorCode {
    QUESTION_NOT_FOUND(40001,"你访问的文章似乎不存在呢",""),
    UNKNOWN_ERROR(50000,"哎呀！服务似乎出问题了！怎么会事呢……","未知错误"),
    UNAUTHORIZED_ACCESS(40101,"无权限访问","");

    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码详情
     */
    private final String description;

    public String getMessage() {
        return message;
    }

    CustomizeErrorCode(int code,String message,String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
