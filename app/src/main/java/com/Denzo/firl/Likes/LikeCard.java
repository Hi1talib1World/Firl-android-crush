package com.Denzo.firl.Likes;

import java.util.ArrayList;
import java.util.List;

public class LikeCard {
    private String userId;
    private String name;
    private String profileImageUrl;
    private String job;
    private String school;
    private int age;
    private List<String> interests;
    private double latitude;
    private double longitude;
    private int activityLevel; // 1-10
    private boolean isVerified;
    private double profileCompleteness; // 0-100
    private int distancePreference;
    private double compatibilityScore;

    public LikeCard(String userId, String name, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.interests = new ArrayList<>();
    }

    public LikeCard(String userId, String name, String profileImageUrl, int age, List<String> interests) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.age = age;
        this.interests = interests;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public double getProfileCompleteness() {
        return profileCompleteness;
    }

    public void setProfileCompleteness(double profileCompleteness) {
        this.profileCompleteness = profileCompleteness;
    }

    public int getDistancePreference() {
        return distancePreference;
    }

    public void setDistancePreference(int distancePreference) {
        this.distancePreference = distancePreference;
    }

    public double getCompatibilityScore() {
        return compatibilityScore;
    }

    public void setCompatibilityScore(double compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }
}
