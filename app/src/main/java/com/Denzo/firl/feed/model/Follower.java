package com.Denzo.firl.feed.model;


import java.util.Calendar;

public class Follower {
    private String profileId;
    private long createdDate;

    public Follower() {
    }

    public Follower(String profileId) {
        this.profileId = profileId;
        this.createdDate = Calendar.getInstance().getTimeInMillis();
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
}