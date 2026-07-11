package com.Denzo.firl;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NavigationTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testBottomNavigation() {
        // Check if LikesFragment is displayed by default (Home)
        onView(withId(R.id.container)).check(matches(isDisplayed()));

        // Navigate to Discover
        onView(withId(R.id.Discover)).perform(click());
        onView(withId(R.id.container)).check(matches(isDisplayed()));

        // Navigate to Conversations (Swipe icon in menu)
        onView(withId(R.id.Swipe)).perform(click());
        onView(withId(R.id.active_recycler_view)).check(matches(isDisplayed()));

        // Navigate to Matches (Chat icon in menu)
        onView(withId(R.id.Chat)).perform(click());
        onView(withId(R.id.mRecyclerView)).check(matches(isDisplayed()));

        // Navigate to Profile
        onView(withId(R.id.Profile)).perform(click());
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()));
    }
}
