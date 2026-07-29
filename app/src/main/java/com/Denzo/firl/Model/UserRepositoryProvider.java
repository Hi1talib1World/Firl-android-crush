package com.Denzo.firl.Model;

import com.Denzo.firl.BuildConfig;

public class UserRepositoryProvider {
    
    public static UserRepository get() {
        // We now use Firebase by default. 
        // You can switch back to MockUserRepository() manually if needed for offline UI testing.
        return new FirebaseUserRepository();
    }
}
