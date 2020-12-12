package com.Denzo.firl.dao;

import androidx.room.Entity;

import com.google.firebase.database.annotations.NotNull;

@Entity
public class AuthUser implements Parcelable {

    @Id
    @NotNull
    private String accessToken;

    @NotNull
    private java.util.Date authTime;
    private int expireIn;

    @NotNull
    private String scope;
    private boolean selected;

    @NotNull
    private String loginId;
    private String name;
    private String avatar;

    @Generated
    public AuthUser() {
    }

    public AuthUser(String accessToken) {
        this.accessToken = accessToken;
    }

    @Generated
    public AuthUser(String accessToken, java.util.Date authTime, int expireIn, String scope, boolean selected, String loginId, String name, String avatar) {
        this.accessToken = accessToken;
        this.authTime = authTime;
        this.expireIn = expireIn;
        this.scope = scope;
        this.selected = selected;
        this.loginId = loginId;
        this.name = name;
        this.avatar = avatar;
    }

    @NotNull
    public String getAccessToken() {
        return accessToken;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAccessToken(@NotNull String accessToken) {
        this.accessToken = accessToken;
    }

    @NotNull
    public java.util.Date getAuthTime() {
        return authTime;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAuthTime(@NotNull java.util.Date authTime) {
        this.authTime = authTime;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    @NotNull
    public String getScope() {
        return scope;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setScope(@NotNull String scope) {
        this.scope = scope;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @NotNull
    public String getLoginId() {
        return loginId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLoginId(@NotNull String loginId) {
        this.loginId = loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeLong(this.authTime != null ? this.authTime.getTime() : -1);
        dest.writeInt(this.expireIn);
        dest.writeString(this.scope);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeString(this.loginId);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
    }

    protected AuthUser(Parcel in) {
        this.accessToken = in.readString();
        long tmpAuthTime = in.readLong();
        this.authTime = tmpAuthTime == -1 ? null : new Date(tmpAuthTime);
        this.expireIn = in.readInt();
        this.scope = in.readString();
        this.selected = in.readByte() != 0;
        this.loginId = in.readString();
        this.name = in.readString();
        this.avatar = in.readString();
    }

    public static final Parcelable.Creator<AuthUser> CREATOR = new Parcelable.Creator<AuthUser>() {
        @Override
        public AuthUser createFromParcel(Parcel source) {
            return new AuthUser(source);
        }

        @Override
        public AuthUser[] newArray(int size) {
            return new AuthUser[size];
        }
    };
}
