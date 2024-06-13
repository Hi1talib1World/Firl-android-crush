package com.Denzo.firl;

import static java.lang.Math.abs;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.Denzo.firl.Matches.MatchesFragment;
import com.Denzo.firl.Model.MatchPerson;
import com.Denzo.firl.Model.MyMatchesPersons;
import com.Denzo.firl.Swipe.SwipeFragment;
import com.Denzo.firl.chat.ChatFragment;
import com.Denzo.firl.databinding.ActivityMainBinding;
import com.Denzo.firl.helpers.HorizontalMarginItemDecoration;
import com.Denzo.firl.listeners.MatchPersonClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MatchPersonClickListener {

    private static final String TAG = "MatchsPersonsActivity";
    ActivityMainBinding binding;
    Context mcontext;
    private List<MatchPerson> data;
    private MyMatchesPersons myMatchesPersons;
    private SwipeFragment swipeFragment = new SwipeFragment();
    private MatchesFragment matchesFragment = new MatchesFragment();
    private ChatFragment chatFragment = new ChatFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Bottom Navigation View
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Load the default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    swipeFragment).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.Home:
                            selectedFragment = matchesFragment;
                            break;
                        case R.id.Chat:
                            selectedFragment = swipeFragment;
                            break;
                        case R.id.Swipe:
                            selectedFragment = chatFragment;
                            break;
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                                selectedFragment).commit();
                    }
                    return true;
                }
            };


    @Override
    public void onScrollPagerItemClick(MatchPerson personCard, ImageView imageView) {
        MyUtilsApp.showLog(TAG, "LogD onScrollPagerItemClick : " + personCard.toString());
        MyUtilsApp.showToast(mcontext, personCard.getName());
        // Now, this has dynamic data from myMatchesCourses.getData();.
        // Could use the Id as unique value for go to new activity
        // Intent intentGetStarted;
        // intentGetStarted = new Intent(mcontext, YourActivity.class);
        // startActivity(intentGetStarted);
    }
}
