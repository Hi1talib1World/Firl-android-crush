package com.Denzo.firl.Model;

import androidx.lifecycle.LiveData;
import java.util.List;

public interface UserRepository {
    void loginUser(String email, String password, Callback<User> callback);
    void registerUser(User user, String password, Callback<Boolean> callback);
    void createUser(User user, Callback<Boolean> callback);
    void getUser(String uid, Callback<User> callback);
    void updateProfile(User user, Callback<Boolean> callback);
    void reportUser(String reporterId, String reportedId, String reason, Callback<Boolean> callback);
    void verifyUser(String uid, String method, Callback<Boolean> callback);
    void getPotentialMatches(String sex, Callback<List<User>> callback);
    void getMatches(String uid, Callback<List<User>> callback);
    void getUsersByInterest(String interest, Callback<List<User>> callback);
    void resetPassword(String email, Callback<Boolean> callback);
    void swipeUser(String currentUid, String targetUid, String type, Callback<Boolean> callback);
    void sendMessage(String senderUid, String receiverUid, com.Denzo.firl.Model.ChatMessage message, Callback<Boolean> callback);
    void uploadProfileImage(String uid, android.net.Uri imageUri, Callback<String> callback);

    interface Callback<T> {
        void onResponse(T response);
        void onError(Exception e);
    }
}
