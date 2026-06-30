package com.Denzo.firl.Model;

import java.util.List;
import java.util.Map;

public class User {

    private String uid;
    private String name;
    private String email;
    private String profileImageUrl;
    private String bio;
    private String sex;
    private String phone;
    private int age;

    // Stage 1: New fields for Matching & Verification
    private List<String> interests;
    private Map<String, Object> preferences;
    private boolean isVerified;
    private String verificationLevel;

    // Location intelligence
    private double latitude;
    private double longitude;
    private String city;

    // Demographics & Lifestyle
    private String job;
    private String school;
    private int height;
    private int activityLevel; // 1-10
    private String zodiac;
    private String smoking;
    private String drinking;
    private String relationshipGoal;
    private List<String> lifestyleTags;
    private Map<String, String> prompts;
    private List<String> mediaUrls;

    // Discovery Preferences
    private int minAgePreference = 18;
    private int maxAgePreference = 50;
    private int distancePreference = 50;

    // Privacy
    private boolean isIncognito = false;
    private boolean isHidden = false;

    public User() {}

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getActivityLevel() { return activityLevel; }
    public void setActivityLevel(int activityLevel) { this.activityLevel = activityLevel; }

    public String getZodiac() { return zodiac; }
    public void setZodiac(String zodiac) { this.zodiac = zodiac; }

    public String getSmoking() { return smoking; }
    public void setSmoking(String smoking) { this.smoking = smoking; }

    public String getDrinking() { return drinking; }
    public void setDrinking(String drinking) { this.drinking = drinking; }

    public String getRelationshipGoal() { return relationshipGoal; }
    public void setRelationshipGoal(String relationshipGoal) { this.relationshipGoal = relationshipGoal; }

    public Map<String, String> getPrompts() { return prompts; }
    public void setPrompts(Map<String, String> prompts) { this.prompts = prompts; }

    public List<String> getLifestyleTags() { return lifestyleTags; }
    public void setLifestyleTags(List<String> lifestyleTags) { this.lifestyleTags = lifestyleTags; }

    public List<String> getMediaUrls() { return mediaUrls; }
    public void setMediaUrls(List<String> mediaUrls) { this.mediaUrls = mediaUrls; }

    public int getMinAgePreference() { return minAgePreference; }
    public void setMinAgePreference(int minAgePreference) { this.minAgePreference = minAgePreference; }

    public int getMaxAgePreference() { return maxAgePreference; }
    public void setMaxAgePreference(int maxAgePreference) { this.maxAgePreference = maxAgePreference; }

    public int getDistancePreference() { return distancePreference; }
    public void setDistancePreference(int distancePreference) { this.distancePreference = distancePreference; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

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

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public double calculateProfileCompleteness() {
        double completeness = 0;
        if (profileImageUrl != null && !profileImageUrl.isEmpty() && !profileImageUrl.equals("default")) completeness += 20;
        if (bio != null && !bio.isEmpty()) completeness += 20;
        if (interests != null && !interests.isEmpty()) completeness += 20;
        if (job != null && !job.isEmpty()) completeness += 10;
        if (school != null && !school.isEmpty()) completeness += 10;
        if (isVerified) completeness += 20;
        return completeness;
    }
}
