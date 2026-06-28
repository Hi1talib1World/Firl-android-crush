package com.Denzo.firl;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.Denzo.firl.Conversations.ConversationsFragment;
import com.Denzo.firl.Discover.DiscoverFragment;
import com.Denzo.firl.Matches.MatchesFragment;
import com.Denzo.firl.Profile.ProfileFragment;
import com.Denzo.firl.Likes.LikesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private LikesFragment likesFragment;
    private DiscoverFragment discoverFragment;
    private MatchesFragment matchesFragment;
    private ConversationsFragment conversationsFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(navListener);

        // Set the default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, getLikesFragment()).commit();
        }
    }

    private LikesFragment getLikesFragment() {
        if (likesFragment == null) likesFragment = new LikesFragment();
        return likesFragment;
    }

    private DiscoverFragment getDiscoverFragment() {
        if (discoverFragment == null) discoverFragment = new DiscoverFragment();
        return discoverFragment;
    }

    private MatchesFragment getMatchesFragment() {
        if (matchesFragment == null) matchesFragment = new MatchesFragment();
        return matchesFragment;
    }

    private ConversationsFragment getConversationsFragment() {
        if (conversationsFragment == null) conversationsFragment = new ConversationsFragment();
        return conversationsFragment;
    }

    private ProfileFragment getProfileFragment() {
        if (profileFragment == null) profileFragment = new ProfileFragment();
        return profileFragment;
    }

    private BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    int itemId = item.getItemId();
                    if (itemId == R.id.Home) {
                        selectedFragment = getLikesFragment();
                    } else if (itemId == R.id.Discover) {
                        selectedFragment = getDiscoverFragment();
                    } else if (itemId == R.id.Swipe) {
                        selectedFragment = getConversationsFragment();
                    } else if (itemId == R.id.Chat) {
                        selectedFragment = getMatchesFragment();
                    } else if (itemId == R.id.Profile) {
                        selectedFragment = getProfileFragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
                    }
                    return true;
                }
            };
}
