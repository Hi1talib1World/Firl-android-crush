package com.Denzo.firl.Conversations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Denzo.firl.Matches.MatchesAdapter;
import com.Denzo.firl.Model.ActiveUser;
import com.Denzo.firl.Model.MatchPerson;
import com.Denzo.firl.R;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.widget.TextView;
import android.content.Intent;
import com.Denzo.firl.chat.ChatFragment;

import com.Denzo.firl.Model.User;
import com.Denzo.firl.Model.UserRepository;
import com.Denzo.firl.Model.UserRepositoryProvider;
import com.google.firebase.auth.FirebaseAuth;

public class ConversationsFragment extends Fragment implements ActiveUserAdapter.OnActiveUserClickListener {

    private RecyclerView activeRecyclerView, matchesRecyclerView;
    private ActiveUserAdapter activeAdapter;
    private MatchesAdapter matchesAdapter;
    private List<ActiveUser> activeUsers;
    private List<MatchPerson> conversations;
    private UserRepository userRepository;
    private TextView emptyText;
    private String currentUid;
    private View convProgress;
    private boolean isLoading = false;

    public ConversationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);

        userRepository = UserRepositoryProvider.get();
        currentUid = FirebaseAuth.getInstance().getCurrentUser() != null ? 
                     FirebaseAuth.getInstance().getCurrentUser().getUid() : "mock_uid";

        activeUsers = new ArrayList<>();
        conversations = new ArrayList<>();

        convProgress = view.findViewById(R.id.conv_progress);
        emptyText = view.findViewById(R.id.conv_empty_text);
        activeRecyclerView = view.findViewById(R.id.active_recycler_view);
        activeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        activeAdapter = new ActiveUserAdapter(activeUsers, this);
        activeRecyclerView.setAdapter(activeAdapter);

        matchesRecyclerView = view.findViewById(R.id.matche_recycler_view);
        matchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        matchesAdapter = new MatchesAdapter(getContext(), conversations);
        matchesRecyclerView.setAdapter(matchesAdapter);

        if (currentUid != null) {
            loadConversations();
        }

        return view;
    }

    private void loadConversations() {
        if (isLoading) return;
        isLoading = true;
        if (convProgress != null) convProgress.setVisibility(View.VISIBLE);

        userRepository.getMatches(currentUid, new UserRepository.Callback<List<User>>() {
            @Override
            public void onResponse(List<User> users) {
                isLoading = false;
                if (isAdded()) {
                    if (convProgress != null) convProgress.setVisibility(View.GONE);
                    activeUsers.clear();
                    conversations.clear();
                    for (User user : users) {
                        activeUsers.add(new ActiveUser(user.getName(), user.getProfileImageUrl()));
                        conversations.add(new MatchPerson(0, user.getName(), "No messages yet", user.getProfileImageUrl()));
                    }
                    activeAdapter.notifyDataSetChanged();
                    matchesAdapter.notifyDataSetChanged();

                    if (emptyText != null) {
                        emptyText.setVisibility(users.isEmpty() ? View.VISIBLE : View.GONE);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                isLoading = false;
                if (isAdded()) {
                    if (convProgress != null) convProgress.setVisibility(View.GONE);
                    Log.e("ConversationsFragment", "Error loading conversations", e);
                }
            }
        });
    }

    @Override
    public void onUserClick(ActiveUser user) {
        Intent intent = new Intent(getContext(), ChatFragment.class);
        intent.putExtra("matchName", user.getName());
        intent.putExtra("matchImageUrl", user.getImageUrl());
        startActivity(intent);
    }
}
