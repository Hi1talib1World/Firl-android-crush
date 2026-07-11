package com.Denzo.firl.Model;

import android.os.Handler;
import android.os.Looper;
import com.Denzo.firl.App;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MockUserRepository implements UserRepository {

    private static final int LATENCY_MS = 1500;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();
    private final UserDao userDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public MockUserRepository() {
        userDao = AppDatabase.getInstance(App.getInstance()).userDao();
    }

    @Override
    public void loginUser(String email, String password, Callback<User> callback) {
        handler.postDelayed(() -> {
            if (email.contains("error")) {
                callback.onError(new Exception("Invalid credentials"));
            } else {
                User user = new User();
                user.setUid("mock_uid");
                user.setName("Mock User");
                executor.execute(() -> userDao.insert(user));
                callback.onResponse(user);
            }
        }, LATENCY_MS);
    }

    @Override
    public void createUser(User user, Callback<Boolean> callback) {
        handler.postDelayed(() -> {
            executor.execute(() -> userDao.insert(user));
            callback.onResponse(true);
        }, LATENCY_MS);
    }

    @Override
    public void getUser(String uid, Callback<User> callback) {
        handler.postDelayed(() -> {
            executor.execute(() -> {
                User localUser = userDao.getUserById(uid);
                if (localUser != null) {
                    handler.post(() -> callback.onResponse(localUser));
                } else {
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
                    userDao.insert(user);
                    handler.post(() -> callback.onResponse(user));
                }
            });
        }, LATENCY_MS);
    }

    @Override
    public void updateProfile(User user, Callback<Boolean> callback) {
        handler.postDelayed(() -> {
            if (random.nextInt(10) == 0) { // 10% failure rate for testing
                callback.onError(new Exception("Mock update failed"));
            } else {
                executor.execute(() -> userDao.insert(user));
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
            executor.execute(() -> {
                List<User> localUsers = sex.equals("All") ? userDao.getAllUsers() : userDao.getUsersBySex(sex);
                if (!localUsers.isEmpty() && random.nextBoolean()) { // Sometimes return cache
                     handler.post(() -> callback.onResponse(localUsers));
                     return;
                }
                
                List<User> users = new ArrayList<>();
                
                // 1. Long Term Relationship
                User u1 = new User();
                u1.setUid("1"); u1.setName("Victoria"); u1.setAge(19); u1.setSex("Female");
                u1.setInterests(Arrays.asList("Long Term Relationship", "Music", "Dancing"));
                u1.setJob("Designer"); u1.setSchool("NYU");
                u1.setActivityLevel(7);
                u1.setProfileImageUrl("https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=800&q=80");
                u1.setLatitude(40.7150); u1.setLongitude(-74.0080);
                u1.setCity("New York");
                u1.setVerified(true);
                
                // 2. Travel Partner
                User u2 = new User();
                u2.setUid("2"); u2.setName("Jane"); u2.setAge(22); u2.setSex("Female");
                u2.setInterests(Arrays.asList("Travel Partner", "Photography", "Outdoor Adventures"));
                u2.setJob("Engineer"); u2.setSchool("MIT");
                u2.setActivityLevel(10);
                u2.setProfileImageUrl("https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=800&q=80");
                u2.setLatitude(42.3601); u2.setLongitude(-71.0589);
                u2.setCity("Boston");

                // 3. Gamer / Techie
                User u3 = new User();
                u3.setUid("3"); u3.setName("Alex"); u3.setAge(24); u3.setSex("Male");
                u3.setInterests(Arrays.asList("Gamer", "Techie", "Short Term Fun"));
                u3.setJob("DevOps"); u3.setSchool("UCLA");
                u3.setActivityLevel(4);
                u3.setProfileImageUrl("https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=800&q=80");
                u3.setLatitude(34.0522); u3.setLongitude(-118.2437);
                u3.setCity("Los Angeles");

                users.add(u1); users.add(u2); users.add(u3);
                
                userDao.insertAll(users);
                
                List<User> filtered = new ArrayList<>();
                if (sex.equals("All")) {
                    filtered.addAll(users);
                } else {
                    for (User u : users) {
                        if (u.getSex().equalsIgnoreCase(sex)) filtered.add(u);
                    }
                }
                
                handler.post(() -> callback.onResponse(filtered));
            });
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
        }, 500); 
    }

    @Override
    public void resetPassword(String email, Callback<Boolean> callback) {
        handler.postDelayed(() -> callback.onResponse(true), 500);
    }

    @Override
    public void swipeUser(String currentUid, String targetUid, String type, Callback<Boolean> callback) {
        handler.postDelayed(() -> {
            // Mock match: 30% chance if like or superlike
            boolean isMatch = (type.equals("like") || type.equals("superlike")) && random.nextInt(10) < 3;
            callback.onResponse(isMatch);
        }, 500);
    }

    @Override
    public void sendMessage(String senderUid, String receiverUid, com.Denzo.firl.Model.ChatMessage message, Callback<Boolean> callback) {
        handler.postDelayed(() -> callback.onResponse(true), 500);
    }
}
