package com.example.moon.enums;

public enum NotificationEnum {
    REPLY_QUESTION(0),
    REPLY_COMMENT(1);

    private final int type;

    public int getType() {
        return type;
    }

    NotificationEnum(int type) {
        this.type = type;
    }
}
