package com.example.moon.enums;

public enum CommentTypeEnum {
    QUESTION(0),
    COMMENT(1);
    private Integer type;


    //判断type是否存在
    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
