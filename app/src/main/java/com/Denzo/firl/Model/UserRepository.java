package com.Denzo.firl.Model;

import androidx.lifecycle.LiveData;
import java.util.List;

public interface UserRepository {
    void loginUser(String email, String password, Callback<User> callback);
    void createUser(User user, Callback<Boolean> callback);
    void getUser(String uid, Callback<User> callback);
    void updateProfile(User user, Callback<Boolean> callback);
    void reportUser(String reporterId, String reportedId, String reason, Callback<Boolean> callback);
    void verifyUser(String uid, String method, Callback<Boolean> callback);
    void getPotentialMatches(String sex, Callback<List<User>> callback);
    void getMatches(String uid, Callback<List<User>> callback);
    void getUsersByInterest(String interest, Callback<List<User>> callback);
    
    interface Callback<T> {
        void onResponse(T response);
        void onError(Exception e);
    }
}
