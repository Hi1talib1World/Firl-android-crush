package com.Denzo.firl.Likes;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.Denzo.firl.R;
import com.bumptech.glide.Glide;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import com.Denzo.firl.Utils.MatchingEngine;
import com.Denzo.firl.Model.User;
import com.Denzo.firl.Model.UserRepository;
import com.Denzo.firl.Model.UserRepositoryProvider;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.auth.FirebaseAuth;

public class LikesFragment extends Fragment {

    private enum SwipeState { LOADING, IDLE, EMPTY }
    private SwipeState currentState = SwipeState.IDLE;

    private LikesArrayAdapter likesArrayAdapter;
    private List<LikeCard> rowItems;
    private LikeCard lastSwipedCard;
    private LikeCard currentCardUser;
    private UserRepository userRepository;
    private String currentUid;
    private View loadingOverlay;
    private SwipeFlingAdapterView flingContainer;
    private ImageButton likeBtn, dislikeBtn, rewindBtn, superLikeBtn, commentBtn;
    private User currentUserObject;
    private String selectedGender = "All";
    private String selectedZodiac = "Any";
    private String selectedHabit = "Any";
    private int selectedMinAge = 18, selectedMaxAge = 100, selectedDistance = 500;

    public LikesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_swipe, container, false);

        loadingOverlay = view.findViewById(R.id.more_frame);
        flingContainer = view.findViewById(R.id.frame);
        
        likeBtn = view.findViewById(R.id.likebtn);
        dislikeBtn = view.findViewById(R.id.dislikebtn);
        rewindBtn = view.findViewById(R.id.rewindbtn);
        superLikeBtn = view.findViewById(R.id.superlikebtn);
        commentBtn = view.findViewById(R.id.commentbtn);

        userRepository = UserRepositoryProvider.get();
        currentUid = FirebaseAuth.getInstance().getCurrentUser() != null ? 
                     FirebaseAuth.getInstance().getCurrentUser().getUid() : "mock_uid";

        rowItems = new ArrayList<>();
        likesArrayAdapter = new LikesArrayAdapter(getContext(), R.layout.item, rowItems);

        flingContainer.setAdapter(likesArrayAdapter);

        if (currentUid != null) {
            setSwipeState(SwipeState.LOADING);
            loadUserDataAndMatches();
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                if (!rowItems.isEmpty()) {
                    lastSwipedCard = rowItems.get(0);
                    rowItems.remove(0);
                    likesArrayAdapter.notifyDataSetChanged();
                }
                
                if (rowItems.isEmpty()) {
                    setSwipeState(SwipeState.EMPTY);
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Toast.makeText(getContext(), "Dislike!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                LikeCard card = (LikeCard) dataObject;
                showMatchDialog(card);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Potential to load more here
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                if (view != null) {
                    view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                    view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                }
            }
        });

        likeBtn.setOnClickListener(v -> {
            if (currentState == SwipeState.IDLE && !rowItems.isEmpty()) {
                flingContainer.getTopCardListener().selectRight();
            }
        });

        dislikeBtn.setOnClickListener(v -> {
            if (currentState == SwipeState.IDLE && !rowItems.isEmpty()) {
                flingContainer.getTopCardListener().selectLeft();
            }
        });

        rewindBtn.setOnClickListener(v -> {
            if (currentState != SwipeState.LOADING && lastSwipedCard != null) {
                rowItems.add(0, lastSwipedCard);
                likesArrayAdapter.notifyDataSetChanged();
                lastSwipedCard = null;
                setSwipeState(SwipeState.IDLE);
                Toast.makeText(getContext(), "Rewind!", Toast.LENGTH_SHORT).show();
            } else if (lastSwipedCard == null) {
                Toast.makeText(getContext(), "Nothing to rewind", Toast.LENGTH_SHORT).show();
            }
        });

        superLikeBtn.setOnClickListener(v -> {
            if (currentState == SwipeState.IDLE && !rowItems.isEmpty()) {
                LikeCard card = rowItems.get(0);
                showMatchDialog(card);
                flingContainer.getTopCardListener().selectRight();
            }
        });

        commentBtn.setOnClickListener(v -> {
            if (!rowItems.isEmpty()) {
                showCommentDialog(rowItems.get(0));
            }
        });

        setupFilters(view);

        return view;
    }

    private void setSwipeState(SwipeState state) {
        this.currentState = state;
        if (loadingOverlay == null || flingContainer == null) return;

        switch (state) {
            case LOADING:
                loadingOverlay.setVisibility(View.VISIBLE);
                flingContainer.setVisibility(View.GONE);
                setButtonsEnabled(false);
                break;
            case IDLE:
                loadingOverlay.setVisibility(View.GONE);
                flingContainer.setVisibility(View.VISIBLE);
                setButtonsEnabled(true);
                break;
            case EMPTY:
                loadingOverlay.setVisibility(View.VISIBLE);
                flingContainer.setVisibility(View.GONE);
                setButtonsEnabled(false);
                rewindBtn.setEnabled(lastSwipedCard != null);
                if (rewindBtn.isEnabled()) rewindBtn.setAlpha(1.0f);
                break;
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        if (likeBtn == null || dislikeBtn == null || superLikeBtn == null || rewindBtn == null) return;
        
        likeBtn.setEnabled(enabled);
        dislikeBtn.setEnabled(enabled);
        superLikeBtn.setEnabled(enabled);
        rewindBtn.setEnabled(enabled);
        
        float alpha = enabled ? 1.0f : 0.5f;
        likeBtn.setAlpha(alpha);
        dislikeBtn.setAlpha(alpha);
        superLikeBtn.setAlpha(alpha);
        rewindBtn.setAlpha(alpha);
    }

    private void loadUserDataAndMatches() {
        userRepository.getUser(currentUid, new UserRepository.Callback<User>() {
            @Override
            public void onResponse(User user) {
                if (user != null) {
                    currentUserObject = user;
                    currentCardUser = convertUserToCard(user);
                    
                    // Initialize filters from user preferences
                    selectedMinAge = user.getMinAgePreference();
                    selectedMaxAge = user.getMaxAgePreference();
                    selectedDistance = user.getDistancePreference();
                    
                    fetchPotentialMatches();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("LikesFragment", "Error loading user data", e);
                setSwipeState(SwipeState.EMPTY);
            }
        });
    }

    private void fetchPotentialMatches() {
        if (currentUserObject == null) return;
        
        String genderToFetch = selectedGender.equals("All") ? "Female" : selectedGender; 
        
        userRepository.getPotentialMatches(genderToFetch, new UserRepository.Callback<List<User>>() {
            @Override
            public void onResponse(List<User> users) {
                if (!isAdded()) return;
                rowItems.clear();
                for (User user : users) {
                    if (!user.getUid().equals(currentUid)) {
                        // Apply current local filters
                        if (user.getAge() >= selectedMinAge && user.getAge() <= selectedMaxAge) {
                        
                        boolean matchZodiac = selectedZodiac.equals("Any") || selectedZodiac.equalsIgnoreCase(user.getZodiac());
                        boolean matchHabit = selectedHabit.equals("Any") || selectedHabit.equalsIgnoreCase(user.getSmoking()) || selectedHabit.equalsIgnoreCase(user.getDrinking());
                        
                        if (matchZodiac && matchHabit) {
                            double dist = MatchingEngine.calculateDistance(currentUserObject.getLatitude(), currentUserObject.getLongitude(), 
                                                           user.getLatitude(), user.getLongitude());
                            
                            if (dist <= selectedDistance) {
                                rowItems.add(convertUserToCard(user));
                            }
                        }
                    }
                    }
                }
                
                if (currentCardUser != null) {
                    MatchingEngine.rankProfiles(currentCardUser, rowItems);
                }
                
                likesArrayAdapter.notifyDataSetChanged();
                setSwipeState(rowItems.isEmpty() ? SwipeState.EMPTY : SwipeState.IDLE);
            }

            @Override
            public void onError(Exception e) {
                Log.e("LikesFragment", "Error loading matches", e);
                setSwipeState(SwipeState.EMPTY);
            }
        });
    }


    private LikeCard convertUserToCard(User user) {
        LikeCard card = new LikeCard(user.getUid(), user.getName(), user.getProfileImageUrl(), user.getAge(), user.getInterests());
        card.setVerified(user.isVerified());
        card.setLatitude(user.getLatitude());
        card.setLongitude(user.getLongitude());
        card.setJob(user.getJob());
        card.setSchool(user.getSchool());
        card.setActivityLevel(user.getActivityLevel());
        card.setDistancePreference(user.getDistancePreference());
        card.setProfileCompleteness(user.calculateProfileCompleteness());
        return card;
    }

    private void showMatchDialog(final LikeCard card) {
        if (!isAdded()) return;
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_match);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            // Full screen feel
            dialog.getWindow().setDimAmount(0.9f);
        }

        ImageView myImage = dialog.findViewById(R.id.my_image);
        ImageView matchImage = dialog.findViewById(R.id.match_image);
        TextView matchText = dialog.findViewById(R.id.match_text);
        TextView iceBreakerText = dialog.findViewById(R.id.ice_breaker_text);
        Button sendMessageBtn = dialog.findViewById(R.id.send_message_btn);
        TextView keepSwiping = dialog.findViewById(R.id.keep_swiping);

        if (currentUserObject != null && currentUserObject.getProfileImageUrl() != null) {
            if (currentUserObject.getProfileImageUrl().equals("default")) {
                myImage.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(this).load(currentUserObject.getProfileImageUrl()).into(myImage);
            }
        }

        Glide.with(this).load(card.getProfileImageUrl()).into(matchImage);
        matchText.setText("You and " + card.getName() + " liked each other.");

        String sharedInterest = "";
        if (currentCardUser != null && currentCardUser.getInterests() != null) {
            for (String interest : card.getInterests()) {
                if (currentCardUser.getInterests().contains(interest)) {
                    sharedInterest = interest;
                    break;
                }
            }
        }
        if (!sharedInterest.isEmpty()) {
            iceBreakerText.setText("Ice Breaker: You both love " + sharedInterest + "!");
            iceBreakerText.setVisibility(View.VISIBLE);
        } else {
            iceBreakerText.setVisibility(View.GONE);
        }

        sendMessageBtn.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(getContext(), "Send message to " + card.getName(), Toast.LENGTH_SHORT).show();
        });

        keepSwiping.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void setupFilters(View view) {
        com.google.android.material.chip.Chip filterSex = view.findViewById(R.id.filter_sex);
        com.google.android.material.chip.Chip filterAge = view.findViewById(R.id.filter_age);
        com.google.android.material.chip.Chip filterDistance = view.findViewById(R.id.filter_distance);
        com.google.android.material.chip.Chip filterZodiac = view.findViewById(R.id.filter_zodiac);
        com.google.android.material.chip.Chip filterHabits = view.findViewById(R.id.filter_habits);

        filterSex.setOnClickListener(v -> {
            String[] genders = {"All", "Male", "Female"};
            new androidx.appcompat.app.AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle("Select Gender")
                .setItems(genders, (dialog, which) -> {
                    selectedGender = genders[which];
                    filterSex.setText(genders[which].equals("All") ? "All Genders" : genders[which]);
                    refreshData();
                }).show();
        });

        filterAge.setOnClickListener(v -> {
            showAgeRangeDialog(filterAge);
        });

        filterDistance.setOnClickListener(v -> {
            String[] ranges = {"10km", "25km", "50km", "100km", "200km", "500km"};
            new androidx.appcompat.app.AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle("Search Radius")
                .setItems(ranges, (dialog, which) -> {
                    selectedDistance = Integer.parseInt(ranges[which].replace("km", ""));
                    filterDistance.setText("Distance: " + ranges[which]);
                    refreshData();
                }).show();
        });

        filterZodiac.setOnClickListener(v -> {
            String[] zodiacs = {"Any", "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"};
            new androidx.appcompat.app.AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle("Select Zodiac")
                .setItems(zodiacs, (dialog, which) -> {
                    selectedZodiac = zodiacs[which];
                    filterZodiac.setText(zodiacs[which].equals("Any") ? "Any Zodiac" : zodiacs[which]);
                    refreshData();
                }).show();
        });

        filterHabits.setOnClickListener(v -> {
            String[] habits = {"Any", "Never Smokes", "Social Smoker", "Never Drinks", "Social Drinker"};
            new androidx.appcompat.app.AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle("Select Habits")
                .setItems(habits, (dialog, which) -> {
                    selectedHabit = habits[which].contains("Smokes") ? "Never" : (habits[which].contains("Smoker") ? "Socially" : "Any");
                    // Mapping habit to simple logic for now
                    filterHabits.setText(habits[which]);
                    refreshData();
                }).show();
        });
    }

    private void refreshData() {
        setSwipeState(SwipeState.LOADING);
        fetchPotentialMatches();
    }

    private void showAgeRangeDialog(com.google.android.material.chip.Chip filterChip) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_age_range);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView ageText = dialog.findViewById(R.id.dialog_age_text);
        RangeSlider slider = dialog.findViewById(R.id.dialog_age_slider);
        Button confirmBtn = dialog.findViewById(R.id.dialog_age_confirm);

        // Set current values if possible, else defaults
        slider.setValues(18f, 35f);
        ageText.setText("18 - 35");

        slider.addOnChangeListener((s, value, fromUser) -> {
            List<Float> values = s.getValues();
            int min = Math.round(values.get(0));
            int max = Math.round(values.get(1));
            ageText.setText(min + " - " + max);
        });

        confirmBtn.setOnClickListener(v -> {
            List<Float> values = slider.getValues();
            selectedMinAge = Math.round(values.get(0));
            selectedMaxAge = Math.round(values.get(1));
            filterChip.setText("Age: " + selectedMinAge + "-" + selectedMaxAge);
            dialog.dismiss();
            refreshData();
        });

        dialog.show();
    }

    private void showCommentDialog(final LikeCard card) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comment);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView title = dialog.findViewById(R.id.comment_title);
        android.widget.EditText input = dialog.findViewById(R.id.comment_input);
        Button sendBtn = dialog.findViewById(R.id.comment_send);

        title.setText("Comment on " + card.getName());

        sendBtn.setOnClickListener(v -> {
            String comment = input.getText().toString();
            if (!comment.isEmpty()) {
                Toast.makeText(getContext(), "Comment sent to " + card.getName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
