package com.Denzo.firl.Utils;

import com.Denzo.firl.Likes.LikeCard;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MatchingEngine {

    /**
     * Compatibility Score =
     * 30% shared interests
     * 25% location (within user's preference)
     * 20% age preference
     * 15% activity level
     * 10% profile completeness
     */
    public static double calculateScore(LikeCard currentUser, LikeCard targetUser) {
        double interestScore = calculateInterestScore(currentUser.getInterests(), targetUser.getInterests()) * 0.30;
        double locationScore = calculateLocationScore(currentUser, targetUser) * 0.25;
        double ageScore = calculateAgeScore(currentUser.getAge(), targetUser.getAge()) * 0.20;
        double activityScore = calculateActivityScore(currentUser.getActivityLevel(), targetUser.getActivityLevel()) * 0.15;
        double completenessScore = targetUser.getProfileCompleteness() * 0.10;

        return interestScore + locationScore + ageScore + activityScore + completenessScore;
    }

    private static double calculateInterestScore(List<String> interests1, List<String> interests2) {
        if (interests1 == null || interests2 == null || interests1.isEmpty() || interests2.isEmpty()) return 0;
        int sharedCount = 0;
        for (String interest : interests1) {
            for (String targetInterest : interests2) {
                if (interest.equalsIgnoreCase(targetInterest)) {
                    sharedCount++;
                    break;
                }
            }
        }
        return (double) sharedCount / Math.max(interests1.size(), 1) * 100;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private static double calculateLocationScore(LikeCard currentUser, LikeCard targetUser) {
        double dist = calculateDistance(currentUser.getLatitude(), currentUser.getLongitude(),
                targetUser.getLatitude(), targetUser.getLongitude());
        
        int pref = currentUser.getDistancePreference();
        if (pref <= 0) pref = 50; // Default fallback

        if (dist <= 1.0) return 100; // Extremely close
        double score = 100 - (dist / pref * 100);
        return Math.max(0, score);
    }

    private static double calculateAgeScore(int age1, int age2) {
        int diff = Math.abs(age1 - age2);
        if (diff <= 2) return 100;
        if (diff <= 5) return 80;
        if (diff <= 10) return 50;
        return 20;
    }

    private static double calculateActivityScore(int level1, int level2) {
        int diff = Math.abs(level1 - level2);
        return Math.max(0, 100 - (diff * 15));
    }

    public static void rankProfiles(LikeCard currentUser, List<LikeCard> profiles) {
        for (LikeCard profile : profiles) {
            profile.setCompatibilityScore(calculateScore(currentUser, profile));
        }
        Collections.sort(profiles, new Comparator<LikeCard>() {
            @Override
            public int compare(LikeCard o1, LikeCard o2) {
                return Double.compare(o2.getCompatibilityScore(), o1.getCompatibilityScore());
            }
        });
    }
}
