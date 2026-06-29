package com.Denzo.firl.Discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.Denzo.firl.Model.User;
import com.Denzo.firl.Model.UserRepository;
import com.Denzo.firl.Model.UserRepositoryProvider;
import com.Denzo.firl.R;
import com.Denzo.firl.Utils.Constants;
import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import com.bumptech.glide.Glide;

public class DiscoverFragment extends Fragment implements InterestAdapter.OnInterestClickListener, DiscoverUserAdapter.OnUserClickListener {

    private RecyclerView interestRecyclerView, userRecyclerView;
    private InterestAdapter interestAdapter;
    private DiscoverUserAdapter userAdapter;
    private EditText searchBar;
    private TextView emptyText;
    private List<User> discoveredUsers = new ArrayList<>();
    private UserRepository userRepository;
    private View searchProgress;
    private boolean isSearching = false;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRepository = UserRepositoryProvider.get();

        interestRecyclerView = view.findViewById(R.id.interest_recycler_view);
        userRecyclerView = view.findViewById(R.id.discover_recycler_view);
        searchProgress = view.findViewById(R.id.search_progress);
        searchBar = view.findViewById(R.id.discover_search_bar);
        emptyText = view.findViewById(R.id.discover_empty_text);

        setupInterestList();
        setupUserList();
        setupSearch();

        // Load default interest: "Long Term Relationship"
        onInterestClick("Long Term Relationship");
    }

    private void setupSearch() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    onInterestClick("Long Term Relationship");
                } else if (query.length() > 1) {
                    performSearch(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch(String query) {
        if (isSearching) return;
        
        isSearching = true;
        if (searchProgress != null) searchProgress.setVisibility(View.VISIBLE);
        discoveredUsers.clear();
        userAdapter.notifyDataSetChanged();

        // Use a generic fetch and filter locally for search
        userRepository.getPotentialMatches("Female", new UserRepository.Callback<List<User>>() {
            @Override
            public void onResponse(List<User> users) {
                isSearching = false;
                if (isAdded()) {
                    if (searchProgress != null) searchProgress.setVisibility(View.GONE);
                    
                    for (User user : users) {
                        boolean matchName = user.getName() != null && user.getName().toLowerCase().contains(query.toLowerCase());
                        boolean matchInterest = false;
                        if (user.getInterests() != null) {
                            for (String interest : user.getInterests()) {
                                if (interest.toLowerCase().contains(query.toLowerCase())) {
                                    matchInterest = true;
                                    break;
                                }
                            }
                        }
                        
                        if (matchName || matchInterest) {
                            discoveredUsers.add(user);
                        }
                    }
                    
                    userAdapter.notifyDataSetChanged();
                    if (emptyText != null) {
                        emptyText.setVisibility(discoveredUsers.isEmpty() ? View.VISIBLE : View.GONE);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                isSearching = false;
                if (isAdded() && searchProgress != null) searchProgress.setVisibility(View.GONE);
            }
        });
    }

    private void setupInterestList() {
        interestAdapter = new InterestAdapter(Constants.INTERESTS, this);
        interestRecyclerView.setAdapter(interestAdapter);
    }

    private void setupUserList() {
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new DiscoverUserAdapter(getContext(), discoveredUsers, this);
        userRecyclerView.setAdapter(userAdapter);
    }

    @Override
    public void onInterestClick(String interest) {
        if (isSearching) return;
        
        isSearching = true;
        if (searchProgress != null) searchProgress.setVisibility(View.VISIBLE);
        discoveredUsers.clear();
        userAdapter.notifyDataSetChanged();

        userRepository.getUsersByInterest(interest, new UserRepository.Callback<List<User>>() {
            @Override
            public void onResponse(List<User> users) {
                isSearching = false;
                if (isAdded()) {
                    if (searchProgress != null) searchProgress.setVisibility(View.GONE);
                    discoveredUsers.addAll(users);
                    userAdapter.notifyDataSetChanged();
                    
                    if (emptyText != null) {
                        emptyText.setVisibility(users.isEmpty() ? View.VISIBLE : View.GONE);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                isSearching = false;
                if (isAdded()) {
                    if (searchProgress != null) searchProgress.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error loading users", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onUserClick(User user) {
        showUserProfilePreview(user);
    }

    private void showUserProfilePreview(User user) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_user_preview);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        ImageView profileImage = dialog.findViewById(R.id.preview_image);
        TextView nameAge = dialog.findViewById(R.id.preview_name_age);
        TextView bio = dialog.findViewById(R.id.preview_bio);
        Button actionBtn = dialog.findViewById(R.id.preview_action_btn);

        nameAge.setText(user.getName() + ", " + user.getAge());
        bio.setText(user.getBio() != null ? user.getBio() : "No bio provided.");
        
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().equals("default")) {
            Glide.with(this).load(user.getProfileImageUrl()).into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.monkey);
        }

        actionBtn.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Liked " + user.getName(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}

