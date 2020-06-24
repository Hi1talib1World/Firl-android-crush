package com.Denzo.firl.search;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.Denzo.firl.R;
import com.google.android.material.tabs.TabLayout;

public class SearchActivity extends BaseActivity<SearchView, SearchPresenter> implements SearchView {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private TabsPagerAdapter tabsAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private android.support.v7.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initContentView();
    }

    @NonNull
    @Override
    public SearchPresenter createPresenter() {
        if (presenter == null) {
            return new SearchPresenter(this);
        }
        return presenter;
    }

    private void initContentView() {
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tabLayout);

        initTabs();
    }

    private void initTabs() {

        tabsAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());

        Bundle argsPostsTab = new Bundle();
        tabsAdapter.addTab(SearchPostsFragment.class, argsPostsTab, getResources().getString(R.string.posts_tab_title));

        Bundle argsUsersTab = new Bundle();
        tabsAdapter.addTab(SearchUsersFragment.class, argsUsersTab, getResources().getString(R.string.users_tab_title));

        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                search(searchView.getQuery().toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void search(String searchText) {
        Fragment fragment = tabsAdapter.getItem(viewPager.getCurrentItem());
        ((Searchable)fragment).search(searchText);
        LogUtil.logDebug(TAG, "search text: " + searchText);
    }

    private void initSearch(MenuItem searchMenuItem) {
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (android.support.v7.widget.SearchView) searchMenuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchMenuItem.expandActionView();

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });

        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        initSearch(searchMenuItem);

        return true;
    }

}