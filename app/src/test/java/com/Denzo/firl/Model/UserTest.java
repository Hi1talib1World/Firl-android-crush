package com.Denzo.firl.Model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collections;

public class UserTest {

    @Test
    public void testCalculateProfileCompleteness_Empty() {
        User user = new User();
        assertEquals(0.0, user.calculateProfileCompleteness(), 0.01);
    }

    @Test
    public void testCalculateProfileCompleteness_Full() {
        User user = new User();
        user.setProfileImageUrl("http://example.com/image.jpg");
        user.setBio("Hello, I am a user.");
        user.setInterests(Collections.singletonList("Coding"));
        user.setJob("Developer");
        user.setSchool("University");
        user.setVerified(true);
        
        assertEquals(100.0, user.calculateProfileCompleteness(), 0.01);
    }

    @Test
    public void testCalculateProfileCompleteness_Partial() {
        User user = new User();
        user.setProfileImageUrl("http://example.com/image.jpg");
        user.setBio("Hello, I am a user.");
        
        // 20 (image) + 20 (bio) = 40
        assertEquals(40.0, user.calculateProfileCompleteness(), 0.01);
    }
}
