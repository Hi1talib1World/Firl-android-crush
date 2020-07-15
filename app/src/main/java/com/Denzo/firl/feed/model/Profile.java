package com.Denzo.firl.feed.model;

import com.Denzo.firl.feed.enums.ItemType;
import com.Denzo.firl.feed.model.LazyLoading;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;

@IgnoreExtraProperties
public class Profile implements Serializable, LazyLoading {

    private String id;
    private String username;
    private String email;
    private String photoUrl;
    private String age;
    private long likesCount;
    private String registrationToken;
    private ItemType itemType;

    public Profile() {
        // Default constructor required for calls to DataSnapshot.getValue(Profile.class)
    }

    public Profile(String id) {
        this.id = id;
    }

    public Profile(ItemType load) {
        itemType = load;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getage() {
        return age;
    }

    public void setage(String age) {
        this.age = age;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    @Override
    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
}