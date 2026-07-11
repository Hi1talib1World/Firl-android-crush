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
        bottomNav.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Set the default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, getFragment(LikesFragment.class))
                    .commit();
        }
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.Home) {
            selectedFragment = getFragment(LikesFragment.class);
        } else if (itemId == R.id.Discover) {
            selectedFragment = getFragment(DiscoverFragment.class);
        } else if (itemId == R.id.Swipe) {
            selectedFragment = getFragment(ConversationsFragment.class);
        } else if (itemId == R.id.Chat) {
            selectedFragment = getFragment(MatchesFragment.class);
        } else if (itemId == R.id.Profile) {
            selectedFragment = getFragment(ProfileFragment.class);
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, selectedFragment)
                    .commit();
            return true;
        }
        return false;
    }

    private Fragment getFragment(Class<? extends Fragment> fragmentClass) {
        if (fragmentClass == LikesFragment.class) {
            if (likesFragment == null) likesFragment = new LikesFragment();
            return likesFragment;
        } else if (fragmentClass == DiscoverFragment.class) {
            if (discoverFragment == null) discoverFragment = new DiscoverFragment();
            return discoverFragment;
        } else if (fragmentClass == MatchesFragment.class) {
            if (matchesFragment == null) matchesFragment = new MatchesFragment();
            return matchesFragment;
        } else if (fragmentClass == ConversationsFragment.class) {
            if (conversationsFragment == null) conversationsFragment = new ConversationsFragment();
            return conversationsFragment;
        } else if (fragmentClass == ProfileFragment.class) {
            if (profileFragment == null) profileFragment = new ProfileFragment();
            return profileFragment;
        }
        return null;
    }
}
