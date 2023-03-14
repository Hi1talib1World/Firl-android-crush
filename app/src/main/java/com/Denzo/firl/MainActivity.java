package com.Denzo.firl;

import static java.lang.Math.abs;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.Denzo.firl.Matches.MatchesFragment;
import com.Denzo.firl.Model.MatchPerson;
import com.Denzo.firl.Model.MyMatchesPersons;
import com.Denzo.firl.Swipe.SwipeFragment;
import com.Denzo.firl.chat.ChatFragment;
import com.Denzo.firl.databinding.ActivityMainBinding;
import com.Denzo.firl.helpers.HorizontalMarginItemDecoration;
import com.Denzo.firl.listeners.MatchPersonClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import xute.storyview.StoryModel;
import xute.storyview.StoryView;

public class MainActivity extends AppCompatActivity implements MatchPersonClickListener {

    private static final String TAG = "MatchsPersonsActivity";
    ActivityMainBinding binding;
    Context mcontext;
    private List<MatchPerson> data;
    private MyMatchesPersons myMatchesPersons;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StoryView storyView = findViewById(R.id.storyView);
        storyView.resetStoryVisits();
        ArrayList<StoryModel> uris = new ArrayList<>();
        uris.add(new StoryModel("https://picsum.photos/200/300", "Ankit Kumar", "12:00 PM"));
        uris.add(new StoryModel("https://picsum.photos/200/300", "Ankit Kumar", "12:00 PM"));
        uris.add(new StoryModel("https://picsum.photos/200/300", "Ankit Kumar", "12:00 PM"));
        uris.add(new StoryModel("https://picsum.photos/200/300", "Ankit Kumar", "12:00 PM"));
    }
    SwipeFragment swipeFragment = new SwipeFragment();
    MatchesFragment matchesFragment = new MatchesFragment();
    ChatFragment chatFragment = new ChatFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.swipe:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, swipeFragment).commit();
                return true;

            case R.id.matches:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, matchesFragment).commit();
                return true;

            case R.id.chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, chatFragment).commit();
                return true;
        }
        return false;
    }

    private void setupViewpager(int currentItem, List<MatchPerson> matchCourseList) {
        PersonTopicsViewPager courseTopicsViewPager = new PersonTopicsViewPager(matchCourseList, mcontext, this);
        binding.viewPager.setAdapter(courseTopicsViewPager);
        // set selected item
        binding.viewPager.setCurrentItem(currentItem);
        // You need to retain one page on each side so that the next and previous items are visible
        binding.viewPager.setOffscreenPageLimit(1);
        // Add a PageTransformer that translates the next and previous items horizontally
        // towards the center of the screen, which makes them visible
        int nextItemVisiblePx = (int) getResources().getDimension(R.dimen.viewpager_next_item_visible);
        int currentItemHorizontalMarginPx = (int) getResources().getDimension(R.dimen.viewpager_current_item_horizontal_margin);
        int pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx;
        ViewPager2.PageTransformer pageTransformer = (page, position) -> {
            page.setTranslationX(-pageTranslationX * position);
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.setScaleY(1 - (0.15f * abs(position)));
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        };
        binding.viewPager.setPageTransformer(pageTransformer);
        binding.viewPager.addItemDecoration(new HorizontalMarginItemDecoration(
                mcontext, R.dimen.viewpager_current_item_horizontal_margin_testing,
                R.dimen.viewpager_next_item_visible_testing)
        );
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                countTxtView.setText(String.format(Locale.ENGLISH,"%d/%d", position+1, matchCourseList.size()));

                MyUtilsApp.showLog(TAG, String.format(Locale.ENGLISH, "%d/%d", position + 1, matchCourseList.size()));
            }
        });
    }

    @Override
    public void onScrollPagerItemClick(MatchPerson personCard, ImageView imageView) {
        MyUtilsApp.showLog(TAG, "LogD onScrollPagerItemClick : " + personCard.toString());

        MyUtilsApp.showToast(mcontext, personCard.getName());
        //Now, this has dynamic data from myMatchesCourses.getData();.
        //Could use the Id as unique value for go to new activity
//        Intent intentGetStarted;
//        intentGetStarted = new Intent(mcontext, YourActivity.class);
//        startActivity(intentGetStarted);
    }

}
