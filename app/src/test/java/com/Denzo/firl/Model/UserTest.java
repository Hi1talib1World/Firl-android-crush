package com.Denzo.firl.Model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
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
        // Bio > 100 chars
        user.setBio("This is a very long bio that is definitely more than one hundred characters long. It needs to be quite descriptive to reach the maximum score for the bio section of the profile completeness calculation.");
        // 5+ interests
        user.setInterests(Arrays.asList("Coding", "Gaming", "Reading", "Hiking", "Music"));
        user.setJob("Developer");
        user.setSchool("University");
        user.setVerified(true);
        
        assertEquals(100.0, user.calculateProfileCompleteness(), 0.01);
    }

    @Test
    public void testCalculateProfileCompleteness_Partial() {
        User user = new User();
        user.setProfileImageUrl("http://example.com/image.jpg"); // 20
        user.setBio("Hello, I am a user."); // 10 (short bio)
        
        assertEquals(30.0, user.calculateProfileCompleteness(), 0.01);
    }
}
