package com.Denzo.firl.Model;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MockUserRepository implements UserRepository {

    private static final int LATENCY_MS = 1500;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();

    @Override
    public void loginUser(String email, String password, Callback<User> callback) {
        handler.postDelayed(() -> {
            if (email.contains("error")) {
                callback.onError(new Exception("Invalid credentials"));
            } else {
                User user = new User();
                user.setUid("mock_uid");
                user.setName("Mock User");
                callback.onResponse(user);
            }
        }, LATENCY_MS);
    }

    @Override
    public void createUser(User user, Callback<Boolean> callback) {
        handler.postDelayed(() -> callback.onResponse(true), LATENCY_MS);
    }

    @Override
    public void getUser(String uid, Callback<User> callback) {
        handler.postDelayed(() -> {
            User user = new User();
            user.setUid(uid);
            user.setName("Mock User");
            user.setSex("Male");
            user.setAge(25);
            user.setJob("Lead Developer");
            user.setSchool("Stanford");
            user.setInterests(Arrays.asList("Coding", "Travel", "Music"));
            user.setLifestyleTags(Arrays.asList("Aries", "Coffee Lover", "Techie"));
            user.setProfileImageUrl("default");
            user.setActivityLevel(9);
            user.setLatitude(40.7128); // New York
            user.setLongitude(-74.0060);
            user.setMinAgePreference(18);
            user.setMaxAgePreference(35);
            user.setDistancePreference(100);
            callback.onResponse(user);
        }, LATENCY_MS);
    }

    @Override
    public void updateProfile(User user, Callback<Boolean> callback) {
        handler.postDelayed(() -> {
            if (random.nextInt(10) == 0) { // 10% failure rate for testing
                callback.onError(new Exception("Mock update failed"));
            } else {
                callback.onResponse(true);
            }
        }, LATENCY_MS);
    }

    @Override
    public void reportUser(String reporterId, String reportedId, String reason, Callback<Boolean> callback) {
        handler.postDelayed(() -> callback.onResponse(true), LATENCY_MS);
    }

    @Override
    public void verifyUser(String uid, String method, Callback<Boolean> callback) {
        handler.postDelayed(() -> callback.onResponse(true), LATENCY_MS);
    }

    @Override
    public void getPotentialMatches(String sex, Callback<List<User>> callback) {
        handler.postDelayed(() -> {
            List<User> users = new ArrayList<>();
            
            // 1. Long Term Relationship
            User u1 = new User();
            u1.setUid("1"); u1.setName("Victoria"); u1.setAge(19); u1.setSex(sex);
            u1.setInterests(Arrays.asList("Long Term Relationship", "Music", "Dancing"));
            u1.setJob("Designer"); u1.setSchool("NYU");
            u1.setActivityLevel(7);
            u1.setProfileImageUrl("https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=800&q=80");
            u1.setLatitude(40.7150); u1.setLongitude(-74.0080);
            u1.setVerified(true);
            
            // 2. Travel Partner
            User u2 = new User();
            u2.setUid("2"); u2.setName("Jane"); u2.setAge(22); u2.setSex(sex);
            u2.setInterests(Arrays.asList("Travel Partner", "Photography", "Outdoor Adventures"));
            u2.setJob("Engineer"); u2.setSchool("MIT");
            u2.setActivityLevel(10);
            u2.setProfileImageUrl("https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=800&q=80");
            u2.setLatitude(42.3601); u2.setLongitude(-71.0589);

            // 3. Gamer / Techie
            User u3 = new User();
            u3.setUid("3"); u3.setName("Alex"); u3.setAge(24); u3.setSex(sex);
            u3.setInterests(Arrays.asList("Gamer", "Techie", "Short Term Fun"));
            u3.setJob("DevOps"); u3.setSchool("UCLA");
            u3.setActivityLevel(4);
            u3.setProfileImageUrl("https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=800&q=80");
            u3.setLatitude(34.0522); u3.setLongitude(-118.2437);

            // 4. Fitness / Yoga
            User u4 = new User();
            u4.setUid("4"); u4.setName("Sarah"); u4.setAge(27); u4.setSex(sex);
            u4.setInterests(Arrays.asList("Fitness Enthusiast", "Yoga", "Cooking"));
            u4.setJob("Trainer"); u4.setSchool("State College");
            u4.setActivityLevel(8);
            u4.setProfileImageUrl("https://images.unsplash.com/photo-1438761681033-6461ffad8d80?auto=format&fit=crop&w=800&q=80");
            u4.setLatitude(40.7306); u4.setLongitude(-73.9352);

            // 5. Coffee / Foodie
            User u5 = new User();
            u5.setUid("5"); u5.setName("Emma"); u5.setAge(23); u5.setSex(sex);
            u5.setInterests(Arrays.asList("Coffee Lover", "Foodie", "New Friends"));
            u5.setJob("Chef"); u5.setSchool("Culinary Institute");
            u5.setActivityLevel(6);
            u5.setProfileImageUrl("https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=800&q=80");
            u5.setLatitude(40.7580); u5.setLongitude(-73.9855);
            
            users.add(u1); users.add(u2); users.add(u3); users.add(u4); users.add(u5);
            
            // Add more users for a fuller experience
            for (int i = 6; i <= 15; i++) {
                User extra = new User();
                extra.setUid(String.valueOf(i));
                extra.setName("Person " + i);
                extra.setAge(20 + (i % 10));
                extra.setSex(sex);
                extra.setJob("Explorer");
                extra.setSchool("Adventure Academy");
                extra.setProfileImageUrl("https://i.pravatar.cc/300?u=" + i);
                extra.setLatitude(40.7128 + (i * 0.01));
                extra.setLongitude(-74.0060 + (i * 0.01));
                extra.setInterests(Arrays.asList("Music", "Travel", "Coffee Lover"));
                users.add(extra);
            }
            
            callback.onResponse(users);
        }, LATENCY_MS);
    }

    @Override
    public void getMatches(String uid, Callback<List<User>> callback) {
        getPotentialMatches("Female", callback);
    }

    @Override
    public void getUsersByInterest(String interest, Callback<List<User>> callback) {
        handler.postDelayed(() -> {
            getPotentialMatches("Female", new Callback<List<User>>() {
                @Override
                public void onResponse(List<User> response) {
                    List<User> filtered = new ArrayList<>();
                    for (User u : response) {
                        if (u.getInterests() != null && u.getInterests().contains(interest)) {
                            filtered.add(u);
                        }
                    }
                    callback.onResponse(filtered);
                }

                @Override
                public void onError(Exception e) {
                    callback.onError(e);
                }
            });
        }, 500); // Shorter additional delay for interest filtering
    }
}

