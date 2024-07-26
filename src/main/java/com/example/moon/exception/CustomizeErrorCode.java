package com.example.moon.exception;


//自定义错误码
public enum CustomizeErrorCode {
    SUCCESS(20000,"成功",""),
    TARGET_PARAM_NOT_FOUND(40000, "请求参数错误", ""),
    QUESTION_NOT_FOUND(40001, "你访问的文章似乎不存在呢", ""),
    COMMENT_NOT_FOUND(40002, "回复的评论不存在", ""),

    UNAUTHORIZED_ACCESS(40100, "无权限访问", ""),
    NO_LOGIN(40101, "先登录再操作吧~", ""),

    UNKNOWN_ERROR(50000, "哎呀！服务似乎出问题了！怎么会事呢……", "未知错误"),
    ;

    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码详情
     */
    private final String description;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    CustomizeErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
