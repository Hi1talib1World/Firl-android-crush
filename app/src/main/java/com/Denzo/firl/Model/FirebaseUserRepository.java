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

public class FirebaseUserRepository implements UserRepository {

    private DatabaseReference mDatabase;

    public FirebaseUserRepository() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
                callback.onResponse(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }

    @Override
    public void updateProfile(User user, final Callback<Boolean> callback) {
        mDatabase.child("users").child(user.getUid()).setValue(user)
            .addOnSuccessListener(aVoid -> callback.onResponse(true))
            .addOnFailureListener(e -> callback.onError(e));
    }

    @Override
    public void reportUser(String reporterId, String reportedId, String reason, final Callback<Boolean> callback) {
        String reportId = mDatabase.child("reports").push().getKey();
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
            .addOnSuccessListener(aVoid -> callback.onResponse(true))
            .addOnFailureListener(e -> callback.onError(e));
    }

    @Override
    public void getPotentialMatches(String sex, final Callback<List<User>> callback) {
        mDatabase.child("users").orderByChild("sex").equalTo(sex).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userList.add(snapshot.getValue(User.class));
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
}
