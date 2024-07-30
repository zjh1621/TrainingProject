package com.example.moon.enums;

public enum NotificationStatusEnum {
    UNREAD(0),
    READ(1);

    private final int status;

    public int getType() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
