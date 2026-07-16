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
import com.Denzo.firl.Utils.ActivityTracker;
import com.Denzo.firl.Model.ActivityLog;
import com.Denzo.firl.Model.UserRepository;
import com.Denzo.firl.Model.UserRepositoryProvider;
import com.google.android.material.slider.RangeSlider;
import com.Denzo.firl.Utils.SessionManager;
import com.Denzo.firl.Utils.PulsatorLayout;
import com.google.android.material.snackbar.Snackbar;

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
    private TextView loadingText;
    private Button resetFiltersBtn;
    private SwipeFlingAdapterView flingContainer;
    private ImageButton likeBtn, dislikeBtn, rewindBtn, superLikeBtn;
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
        View view = inflater.inflate(R.layout.fragment_swipe, container, false);

        loadingOverlay = view.findViewById(R.id.more_frame);
        loadingText = view.findViewById(R.id.loading_text);
        resetFiltersBtn = view.findViewById(R.id.reset_filters_btn);
        flingContainer = view.findViewById(R.id.frame);
        
        likeBtn = view.findViewById(R.id.likebtn);
        dislikeBtn = view.findViewById(R.id.dislikebtn);
        rewindBtn = view.findViewById(R.id.rewindbtn);
        superLikeBtn = view.findViewById(R.id.superlikebtn);

        userRepository = UserRepositoryProvider.get();
        currentUid = SessionManager.getInstance().getCurrentUserId();

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
                LikeCard card = (LikeCard) dataObject;
                userRepository.swipeUser(currentUid, card.getUserId(), "dislike", new UserRepository.Callback<Boolean>() {
                    @Override public void onResponse(Boolean isMatch) {}
                    @Override public void onError(Exception e) { Log.e("LikesFragment", "Error swiping left", e); }
                });
                showSnackbar("Disliked " + card.getName(), v -> rewindBtn.performClick());
                ActivityTracker.getInstance().log("Swipe Left", ActivityLog.Status.SUCCESS, "Disliked " + card.getName());
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                LikeCard card = (LikeCard) dataObject;
                userRepository.swipeUser(currentUid, card.getUserId(), "like", new UserRepository.Callback<Boolean>() {
                    @Override
                    public void onResponse(Boolean isMatch) {
                        if (isMatch) {
                            showMatchDialog(card);
                        }
                    }
                    @Override public void onError(Exception e) { Log.e("LikesFragment", "Error swiping right", e); }
                });
                ActivityTracker.getInstance().log("Swipe Right", ActivityLog.Status.SUCCESS, "Liked " + card.getName());
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {}

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
                ActivityTracker.getInstance().log("Rewind", ActivityLog.Status.SUCCESS, "Restored last swiped card.");
            } else if (lastSwipedCard == null) {
                Toast.makeText(getContext(), "Nothing to rewind", Toast.LENGTH_SHORT).show();
            }
        });

        superLikeBtn.setOnClickListener(v -> {
            if (currentState == SwipeState.IDLE && !rowItems.isEmpty()) {
                LikeCard card = rowItems.get(0);
                userRepository.swipeUser(currentUid, card.getUserId(), "superlike", new UserRepository.Callback<Boolean>() {
                    @Override
                    public void onResponse(Boolean isMatch) {
                        if (isMatch) showMatchDialog(card);
                    }
                    @Override public void onError(Exception e) { Log.e("LikesFragment", "Error super-liking", e); }
                });
                flingContainer.getTopCardListener().selectRight();
            }
        });

        setupFilters(view);

        resetFiltersBtn.setOnClickListener(v -> {
            selectedGender = "All";
            selectedZodiac = "Any";
            selectedHabit = "Any";
            selectedMinAge = 18;
            selectedMaxAge = 100;
            selectedDistance = 500;
            refreshData();
        });

        return view;
    }

    private void showSnackbar(String message, View.OnClickListener action) {
        View view = getView();
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            if (action != null) {
                snackbar.setAction("Undo", action);
            }
            snackbar.show();
        }
    }

    private void setSwipeState(SwipeState state) {
        this.currentState = state;
        if (loadingOverlay == null || flingContainer == null) return;
        PulsatorLayout pulsator = loadingOverlay.findViewById(R.id.pulsator);
        switch (state) {
            case LOADING:
                loadingOverlay.setVisibility(View.VISIBLE);
                if (pulsator != null) pulsator.start();
                if (loadingText != null) {
                    loadingText.setText("Finding people near you...");
                    loadingText.setVisibility(View.VISIBLE);
                }
                if (resetFiltersBtn != null) resetFiltersBtn.setVisibility(View.GONE);
                flingContainer.setVisibility(View.GONE);
                setButtonsEnabled(false);
                break;
            case IDLE:
                loadingOverlay.setVisibility(View.GONE);
                if (pulsator != null) pulsator.stop();
                flingContainer.setVisibility(View.VISIBLE);
                setButtonsEnabled(true);
                break;
            case EMPTY:
                loadingOverlay.setVisibility(View.VISIBLE);
                if (pulsator != null) pulsator.stop();
                if (loadingText != null) {
                    loadingText.setText("No more profiles matching your filters.");
                    loadingText.setVisibility(View.VISIBLE);
                }
                if (resetFiltersBtn != null) resetFiltersBtn.setVisibility(View.VISIBLE);
                flingContainer.setVisibility(View.GONE);
                setButtonsEnabled(false);
                rewindBtn.setEnabled(lastSwipedCard != null);
                if (rewindBtn.isEnabled()) rewindBtn.setAlpha(1.0f);
                break;
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        if (likeBtn == null || dislikeBtn == null || superLikeBtn == null || rewindBtn == null) return;
        likeBtn.setEnabled(enabled); dislikeBtn.setEnabled(enabled);
        superLikeBtn.setEnabled(enabled); rewindBtn.setEnabled(enabled);
        float alpha = enabled ? 1.0f : 0.5f;
        likeBtn.setAlpha(alpha); dislikeBtn.setAlpha(alpha);
        superLikeBtn.setAlpha(alpha); rewindBtn.setAlpha(alpha);
    }

    private void loadUserDataAndMatches() {
        userRepository.getUser(currentUid, new UserRepository.Callback<User>() {
            @Override
            public void onResponse(User user) {
                if (user != null) {
                    currentUserObject = user;
                    currentCardUser = convertUserToCard(user);
                    selectedMinAge = user.getMinAgePreference();
                    selectedMaxAge = user.getMaxAgePreference();
                    selectedDistance = user.getDistancePreference();
                    fetchPotentialMatches();
                }
            }
            @Override public void onError(Exception e) { setSwipeState(SwipeState.EMPTY); }
        });
    }

    private void fetchPotentialMatches() {
        if (currentUserObject == null) return;
        String genderToFetch = selectedGender.equals("All") ? "Both" : selectedGender;
        userRepository.getPotentialMatches(genderToFetch, new UserRepository.Callback<List<User>>() {
            @Override
            public void onResponse(List<User> users) {
                if (!isAdded()) return;
                rowItems.clear();
                if (users != null) {
                    for (User user : users) {
                        if (user != null && !user.getUid().equals(currentUid)) {
                            if (user.getAge() >= selectedMinAge && user.getAge() <= selectedMaxAge) {
                                boolean matchZodiac = selectedZodiac.equals("Any") || selectedZodiac.equalsIgnoreCase(user.getZodiac());
                                boolean matchHabit = selectedHabit.equals("Any") || selectedHabit.equalsIgnoreCase(user.getSmoking()) || selectedHabit.equalsIgnoreCase(user.getDrinking());
                                if (matchZodiac && matchHabit) {
                                    double dist = MatchingEngine.calculateDistance(currentUserObject.getLatitude(), currentUserObject.getLongitude(), 
                                                                   user.getLatitude(), user.getLongitude());
                                    if (dist <= selectedDistance) rowItems.add(convertUserToCard(user));
                                }
                            }
                        }
                    }
                }
                if (!rowItems.isEmpty()) {
                    if (currentCardUser != null) MatchingEngine.rankProfiles(currentCardUser, rowItems);
                    likesArrayAdapter.notifyDataSetChanged();
                    setSwipeState(SwipeState.IDLE);
                } else setSwipeState(SwipeState.EMPTY);
            }
            @Override public void onError(Exception e) { if (isAdded()) setSwipeState(SwipeState.EMPTY); }
        });
    }

    private LikeCard convertUserToCard(User user) {
        LikeCard card = new LikeCard(user.getUid(), user.getName(), user.getProfileImageUrl(), user.getAge(), user.getInterests());
        card.setVerified(user.isVerified());
        card.setLatitude(user.getLatitude()); card.setLongitude(user.getLongitude());
        card.setJob(user.getJob()); card.setSchool(user.getSchool());
        card.setActivityLevel(user.getActivityLevel());
        card.setDistancePreference(user.getDistancePreference());
        card.setProfileCompleteness(user.calculateProfileCompleteness());
        return card;
    }

    private void showMatchDialog(final LikeCard card) {
        if (!isAdded()) return;
        final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_match);
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView myImage = dialog.findViewById(R.id.my_image);
        ImageView matchImage = dialog.findViewById(R.id.match_image);
        TextView matchText = dialog.findViewById(R.id.match_text);
        TextView iceBreakerText = dialog.findViewById(R.id.ice_breaker_text);
        Button sendMessageBtn = dialog.findViewById(R.id.send_message_btn);
        TextView keepSwiping = dialog.findViewById(R.id.keep_swiping);

        if (currentUserObject != null && currentUserObject.getProfileImageUrl() != null) {
            if (currentUserObject.getProfileImageUrl().equals("default")) myImage.setImageResource(R.drawable.monkey);
            else Glide.with(this).load(currentUserObject.getProfileImageUrl()).into(myImage);
        }

        Glide.with(this).load(card.getProfileImageUrl()).into(matchImage);
        matchText.setText("You and " + card.getName() + " liked each other.");

        String sharedInterest = "";
        if (currentCardUser != null && currentCardUser.getInterests() != null) {
            for (String interest : card.getInterests()) {
                if (currentCardUser.getInterests().contains(interest)) { sharedInterest = interest; break; }
            }
        }
        if (!sharedInterest.isEmpty()) {
            iceBreakerText.setText("Ice Breaker: You both love " + sharedInterest + "!");
            iceBreakerText.setVisibility(View.VISIBLE);
        } else iceBreakerText.setVisibility(View.GONE);

        sendMessageBtn.setOnClickListener(v -> {
            dialog.dismiss();
            android.content.Intent intent = new android.content.Intent(getContext(), com.Denzo.firl.chat.ChatActivity.class);
            intent.putExtra("matchId", card.getUserId());
            intent.putExtra("matchName", card.getName());
            intent.putExtra("matchImageUrl", card.getProfileImageUrl());
            startActivity(intent);
        });
        keepSwiping.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void setupFilters(View view) {
        com.google.android.material.chip.Chip filterSex = view.findViewById(R.id.filter_sex);
        com.google.android.material.chip.Chip filterAge = view.findViewById(R.id.filter_age);
        com.google.android.material.chip.Chip filterDistance = view.findViewById(R.id.filter_distance);

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

        filterAge.setOnClickListener(v -> showAgeRangeDialog(filterAge));

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
    }

    private void refreshData() {
        setSwipeState(SwipeState.LOADING);
        fetchPotentialMatches();
    }

    private void showAgeRangeDialog(com.google.android.material.chip.Chip filterChip) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_age_range);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        TextView ageText = dialog.findViewById(R.id.dialog_age_text);
        RangeSlider slider = dialog.findViewById(R.id.dialog_age_slider);
        Button confirmBtn = dialog.findViewById(R.id.dialog_age_confirm);
        slider.setValues(18f, 35f); ageText.setText("18 - 35");
        slider.addOnChangeListener((s, value, fromUser) -> {
            List<Float> values = s.getValues();
            ageText.setText(Math.round(values.get(0)) + " - " + Math.round(values.get(1)));
        });
        confirmBtn.setOnClickListener(v -> {
            List<Float> values = slider.getValues();
            selectedMinAge = Math.round(values.get(0));
            selectedMaxAge = Math.round(values.get(1));
            filterChip.setText("Age: " + selectedMinAge + "-" + selectedMaxAge);
            dialog.dismiss(); refreshData();
        });
        dialog.show();
    }
}
