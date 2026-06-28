package com.Denzo.firl.Utils;

import com.Denzo.firl.Likes.LikeCard;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class MatchingEngineTest {

    @Test
    public void testCalculateDistance() {
        // Distance between NYC (40.7128, -74.0060) and Boston (42.3601, -71.0589) is approx 306 km
        double dist = MatchingEngine.calculateDistance(40.7128, -74.0060, 42.3601, -71.0589);
        assertEquals(306.0, dist, 5.0); // Allow 5km margin
    }

    @Test
    public void testCalculateScore_PerfectMatch() {
        LikeCard user1 = new LikeCard("1", "User 1", "url", 25, Arrays.asList("Music", "Coding"));
        user1.setLatitude(40.7128);
        user1.setLongitude(-74.0060);
        user1.setActivityLevel(8);
        user1.setDistancePreference(100);

        LikeCard user2 = new LikeCard("2", "User 2", "url", 25, Arrays.asList("music", "coding")); // Case insensitive
        user2.setLatitude(40.7128);
        user2.setLongitude(-74.0060);
        user2.setActivityLevel(8);
        user2.setProfileCompleteness(100);

        double score = MatchingEngine.calculateScore(user1, user2);
        // Interest: 100 * 0.3 = 30
        // Location: 100 * 0.25 = 25
        // Age: 100 * 0.2 = 20
        // Activity: 100 * 0.15 = 15
        // Completeness: 100 * 0.1 = 10
        // Total: 100
        assertEquals(100.0, score, 0.1);
    }

    @Test
    public void testCalculateScore_LowMatch() {
        LikeCard user1 = new LikeCard("1", "User 1", "url", 25, Arrays.asList("Music"));
        user1.setLatitude(40.7128);
        user1.setLongitude(-74.0060);
        user1.setActivityLevel(10);
        user1.setDistancePreference(50);

        LikeCard user2 = new LikeCard("2", "User 2", "url", 40, Arrays.asList("Hiking"));
        user2.setLatitude(34.0522); // LA
        user2.setLongitude(-118.2437);
        user2.setActivityLevel(1);
        user2.setProfileCompleteness(20);

        double score = MatchingEngine.calculateScore(user1, user2);
        assertTrue(score < 40.0);
    }
}
