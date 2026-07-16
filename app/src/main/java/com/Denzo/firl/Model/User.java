package com.Denzo.firl.Model;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Map;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    private String uid = "";
    private String name;
    private String email;
    private String profileImageUrl;
    private String bio;
    private String sex;
    private String phone;
    private int age;

    private List<String> interests;
    private boolean isVerified;
    private String verificationLevel;

    @Embedded
    private UserLocation location = new UserLocation();

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

    @Embedded
    private DiscoveryPreferences discoveryPreferences = new DiscoveryPreferences();

    // Privacy
    private boolean isIncognito = false;
    private boolean isHidden = false;

    // Constants for Profile Completeness
    public static final double WEIGHT_IMAGE = 20.0;
    public static final double WEIGHT_BIO = 20.0;
    public static final double WEIGHT_INTERESTS = 20.0;
    public static final double WEIGHT_JOB = 10.0;
    public static final double WEIGHT_SCHOOL = 10.0;
    public static final double WEIGHT_VERIFIED = 20.0;

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

    public int getMinAgePreference() { return discoveryPreferences.getMinAge(); }
    public void setMinAgePreference(int minAge) { discoveryPreferences.setMinAge(minAge); }

    public int getMaxAgePreference() { return discoveryPreferences.getMaxAge(); }
    public void setMaxAgePreference(int maxAge) { discoveryPreferences.setMaxAge(maxAge); }

    public int getDistancePreference() { return discoveryPreferences.getDistance(); }
    public void setDistancePreference(int distance) { discoveryPreferences.setDistance(distance); }

    public double getLatitude() { return location.getLatitude(); }
    public void setLatitude(double latitude) { location.setLatitude(latitude); }

    public double getLongitude() { return location.getLongitude(); }
    public void setLongitude(double longitude) { location.setLongitude(longitude); }

    public String getCity() { return location.getCity(); }
    public void setCity(String city) { location.setCity(city); }

    public String getUid() { return uid; }
    public void setUid(@NonNull String uid) { this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public String getVerificationLevel() { return verificationLevel; }
    public void setVerificationLevel(String verificationLevel) { this.verificationLevel = verificationLevel; }

    public boolean isIncognito() { return isIncognito; }
    public void setIncognito(boolean incognito) { isIncognito = incognito; }

    public boolean isHidden() { return isHidden; }
    public void setHidden(boolean hidden) { isHidden = hidden; }

    public UserLocation getLocation() { return location; }
    public void setLocation(UserLocation location) { this.location = location; }

    public DiscoveryPreferences getDiscoveryPreferences() { return discoveryPreferences; }
    public void setDiscoveryPreferences(DiscoveryPreferences discoveryPreferences) { this.discoveryPreferences = discoveryPreferences; }

    public double calculateProfileCompleteness() {
        double score = 0;
        
        // Base Info (Image + Bio)
        if (isNotBlank(profileImageUrl) && !profileImageUrl.equals("default")) {
            score += WEIGHT_IMAGE;
        } else {
            // No profile image
        }

        if (isNotBlank(bio)) {
            if (bio.length() > 100) {
                score += WEIGHT_BIO;
            } else if (bio.length() > 50) {
                score += (WEIGHT_BIO * 0.75);
            } else {
                score += (WEIGHT_BIO * 0.5);
            }
        }

        // Connections & Social
        if (interests != null) {
            if (interests.size() >= 5) {
                score += WEIGHT_INTERESTS;
            } else if (interests.size() >= 3) {
                score += (WEIGHT_INTERESTS * 0.6);
            } else if (interests.size() > 0) {
                score += (WEIGHT_INTERESTS * 0.3);
            }
        }

        // Professional/Education
        if (isNotBlank(job)) {
            score += WEIGHT_JOB;
        }
        if (isNotBlank(school)) {
            score += WEIGHT_SCHOOL;
        }

        // Verification
        if (isVerified) {
            score += WEIGHT_VERIFIED;
        } else if (isNotBlank(verificationLevel)) {
            if (verificationLevel.equals("PENDING")) {
                score += (WEIGHT_VERIFIED * 0.5);
            }
        }
        
        return Math.min(100.0, score);
    }

    private boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
