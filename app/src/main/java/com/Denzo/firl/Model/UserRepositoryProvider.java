package com.Denzo.firl.Model;

import com.Denzo.firl.BuildConfig;

public class UserRepositoryProvider {
    
    public static UserRepository get() {
        if (BuildConfig.DEBUG) {
            return new MockUserRepository();
        }
        return new FirebaseUserRepository();
    }
}
