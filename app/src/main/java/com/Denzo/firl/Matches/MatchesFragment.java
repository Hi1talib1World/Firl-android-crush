package com.Denzo.firl.Matches;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Denzo.firl.Model.MatchPerson;
import com.Denzo.firl.R;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.Denzo.firl.Model.User;
import com.Denzo.firl.Utils.ActivityTracker;
import com.Denzo.firl.Model.ActivityLog;
import com.Denzo.firl.Model.UserRepository;
import com.Denzo.firl.Model.UserRepositoryProvider;
import com.google.firebase.auth.FirebaseAuth;

public class MatchesFragment extends Fragment {

    private androidx.viewpager2.widget.ViewPager2 viewPager;
    private RecyclerView recyclerView, likesYouRecyclerView, activityRecyclerView;
    private MatchesAdapter adapter;
    private LikesYouAdapter likesYouAdapter;
    private FeaturedAdapter featuredAdapter;
    private ActivityLogAdapter activityAdapter;
    private List<MatchPerson> matchHistory;
    private List<User> likesYouList;
    private List<User> featuredList;
    private List<ActivityLog> activityLogs;
    private UserRepository userRepository;
    private TextView emptyMatchesText;
    private String currentUid;
    private View progressBar;
    private boolean isLoading = false;

    public MatchesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        userRepository = UserRepositoryProvider.get();
        currentUid = FirebaseAuth.getInstance().getCurrentUser() != null ? 
                     FirebaseAuth.getInstance().getCurrentUser().getUid() : "mock_uid";

        matchHistory = new ArrayList<>();
        likesYouList = new ArrayList<>();
        featuredList = new ArrayList<>();
        activityLogs = new ArrayList<>(ActivityTracker.getInstance().getLogs());
        
        recyclerView = view.findViewById(R.id.matches_recycler_view);
        likesYouRecyclerView = view.findViewById(R.id.likes_you_recycler_view);
        activityRecyclerView = view.findViewById(R.id.activity_stream_recycler_view);
        viewPager = view.findViewById(R.id.view_pager);
        progressBar = view.findViewById(R.id.matches_progress);
        emptyMatchesText = view.findViewById(R.id.empty_matches_text);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MatchesAdapter(getContext(), matchHistory);
        recyclerView.setAdapter(adapter);

        likesYouRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        likesYouAdapter = new LikesYouAdapter(likesYouList);
        likesYouRecyclerView.setAdapter(likesYouAdapter);

        featuredAdapter = new FeaturedAdapter(featuredList);
        viewPager.setAdapter(featuredAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);

        activityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        activityAdapter = new ActivityLogAdapter(activityLogs);
        activityRecyclerView.setAdapter(activityAdapter);

        ActivityTracker.getInstance().setOnLogAddedListener(log -> {
            if (isAdded()) {
                activityLogs.add(0, log);
                activityAdapter.notifyItemInserted(0);
            }
        });

        if (currentUid != null) {
            loadMatches();
        }

        return view;
    }

    private void loadMatches() {
        if (isLoading) return;
        isLoading = true;
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        userRepository.getMatches(currentUid, new UserRepository.Callback<List<User>>() {
            @Override
            public void onResponse(List<User> users) {
                isLoading = false;
                if (isAdded()) {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    matchHistory.clear();
                    likesYouList.clear();
                    featuredList.clear();
                    
                    likesYouList.addAll(users);
                    if (users.size() > 2) {
                        featuredList.addAll(users.subList(0, 3));
                    } else {
                        featuredList.addAll(users);
                    }

                    for (User user : users) {
                        MatchPerson match = new MatchPerson(
                                0, 
                                user.getName(),
                                "Matched recently",
                                user.getProfileImageUrl()
                        );
                        matchHistory.add(match);
                    }
                    adapter.notifyDataSetChanged();
                    likesYouAdapter.notifyDataSetChanged();
                    featuredAdapter.notifyDataSetChanged();
                    
                    if (matchHistory.isEmpty()) {
                        emptyMatchesText.setVisibility(View.VISIBLE);
                    } else {
                        emptyMatchesText.setVisibility(View.GONE);
                    }
                    
                    ActivityTracker.getInstance().log("Fetch Matches", ActivityLog.Status.SUCCESS, "Found " + users.size() + " matches.");
                }
            }

            @Override
            public void onError(Exception e) {
                isLoading = false;
                if (isAdded()) {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    Log.e("MatchesFragment", "Error loading matches", e);
                    Toast.makeText(getContext(), "Failed to load matches", Toast.LENGTH_SHORT).show();
                    ActivityTracker.getInstance().log("Fetch Matches", ActivityLog.Status.FAILURE, e.getMessage());
                }
            }
        });
    }
}
