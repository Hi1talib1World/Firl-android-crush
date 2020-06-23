package com.Denzo.firl.feed.enums;

public enum ProfileStatus {
    PROFILE_CREATED(0), NOT_AUTHORIZED(1), NO_PROFILE(2);

    int status;

    ProfileStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}