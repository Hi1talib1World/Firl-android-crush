package com.Denzo.firl.dao;

@Entity
public class BookMarkUser {

    @Id
    @NotNull
    private String login;
    private String name;
    private String avatarUrl;
    private Integer followers;
    private Integer following;
    private java.util.Date markTime;

    @Generated
    public BookMarkUser() {
    }

    public BookMarkUser(String login) {
        this.login = login;
    }

    @Generated
    public BookMarkUser(String login, String name, String avatarUrl, Integer followers, Integer following, java.util.Date markTime) {
        this.login = login;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.followers = followers;
        this.following = following;
        this.markTime = markTime;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLogin(@NotNull String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public java.util.Date getMarkTime() {
        return markTime;
    }

    public void setMarkTime(java.util.Date markTime) {
        this.markTime = markTime;
    }

}