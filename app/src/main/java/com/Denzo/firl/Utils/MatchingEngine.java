package com.Denzo.firl.Utils;

import com.Denzo.firl.Likes.LikeCard;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MatchingEngine {

    private static final double WEIGHT_INTERESTS = 0.30;
    private static final double WEIGHT_LOCATION = 0.25;
    private static final double WEIGHT_AGE = 0.20;
    private static final double WEIGHT_ACTIVITY = 0.15;
    private static final double WEIGHT_COMPLETENESS = 0.10;

    public static double calculateScore(LikeCard currentUser, LikeCard targetUser) {
        double interestScore = calculateInterestScore(currentUser.getInterests(), targetUser.getInterests()) * WEIGHT_INTERESTS;
        double locationScore = calculateLocationScore(currentUser, targetUser) * WEIGHT_LOCATION;
        double ageScore = calculateAgeScore(currentUser.getAge(), targetUser.getAge()) * WEIGHT_AGE;
        double activityScore = calculateActivityScore(currentUser.getActivityLevel(), targetUser.getActivityLevel()) * WEIGHT_ACTIVITY;
        double completenessScore = targetUser.getProfileCompleteness() * WEIGHT_COMPLETENESS;

        return interestScore + locationScore + ageScore + activityScore + completenessScore;
    }

    private static double calculateInterestScore(List<String> interests1, List<String> interests2) {
        if (interests1 == null || interests2 == null || interests1.isEmpty() || interests2.isEmpty()) {
            return 0;
        }
        
        int sharedCount = 0;
        for (String interest : interests1) {
            for (String targetInterest : interests2) {
                if (interest.equalsIgnoreCase(targetInterest)) {
                    sharedCount++;
                    break;
                }
            }
        }

        if (sharedCount >= 5) {
            return 100;
        } else if (sharedCount >= 3) {
            return 85;
        } else if (sharedCount == 2) {
            return 60;
        } else if (sharedCount == 1) {
            return 35;
        } else {
            return 0;
        }
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
        if (pref <= 0) {
            pref = 50; 
        }

        if (dist <= 1.0) {
            return 100;
        } else if (dist <= 5.0) {
            return 90;
        } else if (dist <= 10.0) {
            return 80;
        } else if (dist > pref) {
            return 0;
        } else {
            double score = 100 - (dist / pref * 100);
            return Math.max(0, score);
        }
    }

    private static double calculateAgeScore(int age1, int age2) {
        int diff = Math.abs(age1 - age2);
        if (diff <= 2) {
            return 100;
        } else if (diff <= 5) {
            return 80;
        } else if (diff <= 8) {
            return 60;
        } else if (diff <= 12) {
            return 40;
        } else if (diff <= 15) {
            return 20;
        } else {
            return 0;
        }
    }

    private static double calculateActivityScore(int level1, int level2) {
        int diff = Math.abs(level1 - level2);
        if (diff == 0) {
            return 100;
        } else if (diff <= 2) {
            return 80;
        } else if (diff <= 4) {
            return 50;
        } else if (diff <= 6) {
            return 20;
        } else {
            return 0;
        }
    }

    public static void rankProfiles(LikeCard currentUser, List<LikeCard> profiles) {
        if (profiles == null || profiles.isEmpty()) {
            return;
        }

        if (currentUser == null) {
            return;
        }

        for (LikeCard profile : profiles) {
            if (profile != null) {
                profile.setCompatibilityScore(calculateScore(currentUser, profile));
            }
        }
        
        Collections.sort(profiles, new Comparator<LikeCard>() {
            @Override
            public int compare(LikeCard o1, LikeCard o2) {
                if (o1 == null || o2 == null) {
                    return 0;
                }
                return Double.compare(o2.getCompatibilityScore(), o1.getCompatibilityScore());
            }
        });
    }
}
