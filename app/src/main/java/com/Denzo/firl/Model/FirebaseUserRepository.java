package com.Denzo.firl.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.Denzo.firl.App;

public class FirebaseUserRepository implements UserRepository {

    private DatabaseReference mDatabase;
    private UserDao userDao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public FirebaseUserRepository() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userDao = AppDatabase.getInstance(App.getInstance()).userDao();
    }

    @Override
    public void loginUser(String email, String password, final Callback<User> callback) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
                if (authResult.getUser() != null) {
                    getUser(authResult.getUser().getUid(), callback);
                } else {
                    callback.onError(new Exception("Login failed: User is null"));
                }
            })
            .addOnFailureListener(e -> callback.onError(e));
    }

    @Override
    public void createUser(User user, final Callback<Boolean> callback) {
        mDatabase.child("users").child(user.getUid()).setValue(user)
            .addOnSuccessListener(aVoid -> callback.onResponse(true))
            .addOnFailureListener(e -> callback.onError(e));
    }

    @Override
    public void getUser(String uid, final Callback<User> callback) {
        mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    executor.execute(() -> userDao.insert(user));
                    callback.onResponse(user);
                } else {
                    executor.execute(() -> {
                        User localUser = userDao.getUserById(uid);
                        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                            if (localUser != null) {
                                callback.onResponse(localUser);
                            } else {
                                callback.onResponse(null);
                            }
                        });
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                executor.execute(() -> {
                    User localUser = userDao.getUserById(uid);
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        if (localUser != null) {
                            callback.onResponse(localUser);
                        } else {
                            callback.onError(databaseError.toException());
                        }
                    });
                });
            }
        });
    }

    @Override
    public void updateProfile(User user, final Callback<Boolean> callback) {
        mDatabase.child("users").child(user.getUid()).setValue(user)
            .addOnSuccessListener(aVoid -> {
                executor.execute(() -> userDao.insert(user));
                callback.onResponse(true);
            })
            .addOnFailureListener(e -> callback.onError(e));
    }

    @Override
    public void reportUser(String reporterId, String reportedId, String reason, final Callback<Boolean> callback) {
        String reportId = mDatabase.child("reports").push().getKey();
        if (reportId == null) return;
        Map<String, Object> report = new HashMap<>();
        report.put("reporterId", reporterId);
        report.put("reportedId", reportedId);
        report.put("reason", reason);
        report.put("timestamp", System.currentTimeMillis());

        mDatabase.child("reports").child(reportId).setValue(report)
            .addOnSuccessListener(aVoid -> callback.onResponse(true))
            .addOnFailureListener(e -> callback.onError(e));
    }

    @Override
    public void verifyUser(String uid, String method, final Callback<Boolean> callback) {
        Map<String, Object> verification = new HashMap<>();
        verification.put("uid", uid);
        verification.put("method", method);
        verification.put("status", "PENDING");

        mDatabase.child("verification").child(uid).setValue(verification)
            .addOnSuccessListener(aVoid -> {
                executor.execute(() -> {
                    User user = userDao.getUserById(uid);
                    if (user != null) {
                        user.setVerified(false); // Pending
                        userDao.update(user);
                    }
                });
                callback.onResponse(true);
            })
            .addOnFailureListener(e -> callback.onError(e));
    }

    @Override
    public void getPotentialMatches(String sex, final Callback<List<User>> callback) {
        com.google.firebase.database.Query query = sex.equals("All") ? mDatabase.child("users") : mDatabase.child("users").orderByChild("sex").equalTo(sex);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userList.add(snapshot.getValue(User.class));
                }
                executor.execute(() -> userDao.insertAll(userList));
                callback.onResponse(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                executor.execute(() -> {
                    List<User> localUsers = userDao.getUsersBySex(sex);
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        if (!localUsers.isEmpty()) {
                            callback.onResponse(localUsers);
                        } else {
                            callback.onError(databaseError.toException());
                        }
                    });
                });
            }
        });
    }

    @Override
    public void getMatches(String uid, final Callback<List<User>> callback) {
        mDatabase.child("users").child(uid).child("connections").child("matches").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> matches = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        getUser(snapshot.getKey(), new Callback<User>() {
                            @Override
                            public void onResponse(User user) {
                                if (user != null) {
                                    matches.add(user);
                                }
                                // This is a bit tricky with multiple async calls. 
                                // Ideally use a Task.whenAll or similar, but keeping it simple for now.
                                if (matches.size() == dataSnapshot.getChildrenCount()) {
                                    callback.onResponse(matches);
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                // Ignore or handle partial failure
                            }
                        });
                    }
                } else {
                    callback.onResponse(matches);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }

    @Override
    public void getUsersByInterest(String interest, final Callback<List<User>> callback) {
        // Firebase doesn't support array-contains querying natively in Realtime Database as efficiently as Firestore.
        // We will fetch potential matches and filter manually for now, or use a specific index if needed.
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && user.getInterests() != null && user.getInterests().contains(interest)) {
                        userList.add(user);
                    }
                }
                callback.onResponse(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }

    @Override
    public void resetPassword(String email, Callback<Boolean> callback) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnSuccessListener(aVoid -> callback.onResponse(true))
            .addOnFailureListener(e -> callback.onError(e));
    }

    @Override
    public void swipeUser(String currentUid, String targetUid, String type, Callback<Boolean> callback) {
        long timestamp = System.currentTimeMillis();
        Map<String, Object> swipeData = new HashMap<>();
        swipeData.put("type", type);
        swipeData.put("timestamp", timestamp);

        mDatabase.child("swipes").child(currentUid).child(targetUid).setValue(swipeData)
            .addOnSuccessListener(aVoid -> {
                // If it's a like or superlike, check if the other person also liked us
                if (type.equals("like") || type.equals("superlike")) {
                    mDatabase.child("swipes").child(targetUid).child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String otherType = dataSnapshot.child("type").getValue(String.class);
                                if ("like".equals(otherType) || "superlike".equals(otherType)) {
                                    // It's a match!
                                    createMatch(currentUid, targetUid, callback);
                                } else {
                                    callback.onResponse(false); // Liked but no match yet
                                }
                            } else {
                                callback.onResponse(false); // Liked but no match yet
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            callback.onError(databaseError.toException());
                        }
                    });
                } else {
                    callback.onResponse(false); // Disliked, definitely not a match
                }
            })
            .addOnFailureListener(e -> callback.onError(e));
    }

    @Override
    public void sendMessage(String senderUid, String receiverUid, com.Denzo.firl.Model.ChatMessage message, Callback<Boolean> callback) {
        String chatId = senderUid.compareTo(receiverUid) < 0 ? senderUid + "_" + receiverUid : receiverUid + "_" + senderUid;
        String messageId = mDatabase.child("chats").child(chatId).push().getKey();
        if (messageId == null) return;

        mDatabase.child("chats").child(chatId).child(messageId).setValue(message)
            .addOnSuccessListener(aVoid -> callback.onResponse(true))
            .addOnFailureListener(e -> callback.onError(e));
    }

    private void createMatch(String uid1, String uid2, Callback<Boolean> callback) {
        long timestamp = System.currentTimeMillis();
        mDatabase.child("users").child(uid1).child("connections").child("matches").child(uid2).setValue(timestamp);
        mDatabase.child("users").child(uid2).child("connections").child("matches").child(uid1).setValue(timestamp)
            .addOnSuccessListener(aVoid -> callback.onResponse(true))
            .addOnFailureListener(e -> callback.onError(e));
    }
}
