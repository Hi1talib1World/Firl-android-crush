package com.Denzo.firl;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.Denzo.firl.Conversations.ConversationsFragment;
import com.Denzo.firl.Matches.MatchesFragment;
import com.Denzo.firl.Swipe.SwipeFragment;
import com.Denzo.firl.chat.ChatFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SwipeFragment swipeFragment = new SwipeFragment();
    private MatchesFragment matchesFragment = new MatchesFragment();
    private ConversationsFragment conversationsFragment = new ConversationsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Set the default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, swipeFragment).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.Home:
                            selectedFragment = swipeFragment;
                            break;
                        case R.id.Chat:
                            selectedFragment = matchesFragment;
                            break;
                        case R.id.Swipe:
                            selectedFragment = conversationsFragment;
                            break;
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
                    }
                    return true;
                }
            };
}
