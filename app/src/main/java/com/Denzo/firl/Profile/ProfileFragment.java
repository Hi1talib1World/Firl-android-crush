package com.Denzo.firl.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.Denzo.firl.Model.User;
import com.Denzo.firl.Model.UserRepository;
import com.Denzo.firl.Model.UserRepositoryProvider;
import com.Denzo.firl.R;
import com.Denzo.firl.SettingsActivity;
import com.Denzo.firl.WelcomeActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;

import java.util.List;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private CircleImageView profileImage;
    private TextView profileName, profileJobSchool, profileBio, ageRangeText, distanceText, profileStrengthText;
    private LinearProgressIndicator profileStrengthProgress;
    private ChipGroup lifestyleTags, lifestyleBadges;
    private LinearLayout promptsContainer;
    private RangeSlider ageRangeSlider;
    private Slider distanceSlider;
    private View editProfileBtn, prefSaveProgress;
    private ImageView mediaImg1, mediaImg2, mediaImg3;
    private Button settingsBtn, logoutBtn;
    private UserRepository userRepository;
    private String currentUid;
    private User fullUser;
    private boolean isSavingPref = false;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.profile_image);
        profileName = view.findViewById(R.id.profile_name);
        profileJobSchool = view.findViewById(R.id.profile_job_school);
        profileBio = view.findViewById(R.id.profile_bio);
        lifestyleTags = view.findViewById(R.id.lifestyle_tags);
        lifestyleBadges = view.findViewById(R.id.lifestyle_badges);
        promptsContainer = view.findViewById(R.id.prompts_container);
        ageRangeText = view.findViewById(R.id.age_range_text);
        distanceText = view.findViewById(R.id.distance_text);
        profileStrengthText = view.findViewById(R.id.profile_strength_text);
        profileStrengthProgress = view.findViewById(R.id.profile_strength_progress);
        ageRangeSlider = view.findViewById(R.id.age_range_slider);
        distanceSlider = view.findViewById(R.id.distance_slider);
        editProfileBtn = view.findViewById(R.id.edit_profile_btn);
        settingsBtn = view.findViewById(R.id.settings_btn);
        logoutBtn = view.findViewById(R.id.logout_btn);
        prefSaveProgress = view.findViewById(R.id.pref_save_progress);
        
        mediaImg1 = view.findViewById(R.id.media_img_1);
        mediaImg2 = view.findViewById(R.id.media_img_2);
        mediaImg3 = view.findViewById(R.id.media_img_3);

        userRepository = UserRepositoryProvider.get();
        currentUid = FirebaseAuth.getInstance().getCurrentUser() != null ? 
                     FirebaseAuth.getInstance().getCurrentUser().getUid() : "mock_uid";

        setupPreferenceListeners();

        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> logoutUser());

        setupMediaListeners();

        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        loadUserData();

        return view;
    }

    private void setupPreferenceListeners() {
        ageRangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            if (!fromUser) return;
            List<Float> values = slider.getValues();
            int min = Math.round(values.get(0));
            int max = Math.round(values.get(1));
            ageRangeText.setText(String.format(java.util.Locale.US, "%d - %d", min, max));
        });

        ageRangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {}

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                if (fullUser != null) {
                    List<Float> values = slider.getValues();
                    fullUser.setMinAgePreference(Math.round(values.get(0)));
                    fullUser.setMaxAgePreference(Math.round(values.get(1)));
                    savePreferences();
                }
            }
        });

        distanceSlider.addOnChangeListener((slider, value, fromUser) -> {
            if (!fromUser) return;
            int dist = Math.round(value);
            distanceText.setText(String.format(java.util.Locale.US, "%d km", dist));
        });

        distanceSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {}

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                if (fullUser != null) {
                    fullUser.setDistancePreference(Math.round(slider.getValue()));
                    savePreferences();
                }
            }
        });
    }

    private void savePreferences() {
        if (isSavingPref) return;
        isSavingPref = true;
        if (prefSaveProgress != null) prefSaveProgress.setVisibility(View.VISIBLE);

        userRepository.updateProfile(fullUser, new UserRepository.Callback<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                isSavingPref = false;
                if (isAdded() && prefSaveProgress != null) {
                    prefSaveProgress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onError(Exception e) {
                isSavingPref = false;
                if (isAdded()) {
                    if (prefSaveProgress != null) prefSaveProgress.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Failed to save preferences", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setupMediaListeners() {
        mediaImg1.setOnClickListener(v -> showImagePreview(R.drawable.img1));
        mediaImg2.setOnClickListener(v -> showImagePreview(R.drawable.img2));
        mediaImg3.setOnClickListener(v -> showImagePreview(R.drawable.img3));
    }

    private void showImagePreview(int resId) {
        final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_preview);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ImageView fullImage = dialog.findViewById(R.id.preview_image_full);
        fullImage.setImageResource(resId);

        dialog.findViewById(R.id.close_preview).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void setupPrompts(User user) {
        promptsContainer.removeAllViews();
        if (user.getPrompts() != null) {
            for (java.util.Map.Entry<String, String> entry : user.getPrompts().entrySet()) {
                View promptView = getLayoutInflater().inflate(R.layout.item_profile_prompt, promptsContainer, false);
                TextView question = promptView.findViewById(R.id.prompt_question);
                TextView answer = promptView.findViewById(R.id.prompt_answer);
                question.setText(entry.getKey());
                answer.setText(entry.getValue());
                promptsContainer.addView(promptView);
            }
        }
    }

    private void setupLifestyleBadges(User user) {
        lifestyleBadges.removeAllViews();
        addBadge(user.getRelationshipGoal(), R.drawable.love, R.color.pink);
        addBadge(user.getZodiac(), R.drawable.ic_play, R.color.colorAccent);
        if (user.getSmoking() != null && !user.getSmoking().equals("Never")) addBadge("Smokes " + user.getSmoking(), R.drawable.topic, R.color.orange);
        if (user.getDrinking() != null && !user.getDrinking().equals("Never")) addBadge("Drinks " + user.getDrinking(), R.drawable.ic_settings, R.color.link_blue);
    }

    private void addBadge(String text, int iconRes, int colorRes) {
        if (text == null || text.isEmpty()) return;
        Chip chip = new Chip(getContext());
        chip.setText(text);
        chip.setChipIcon(getContext().getDrawable(iconRes));
        chip.setChipIconTint(android.content.res.ColorStateList.valueOf(getContext().getColor(colorRes)));
        chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(0x1AFFFFFF));
        chip.setTextColor(getContext().getColor(android.R.color.white));
        chip.setChipStrokeWidth(0f);
        chip.setTextSize(11f);
        lifestyleBadges.addView(chip);
    }

    private void logoutUser() {
        new androidx.appcompat.app.AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Dialog_Alert)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Logout", (d, w) -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                if (getActivity() != null) getActivity().finish();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }


    private void loadUserData() {
        userRepository.getUser(currentUid, new UserRepository.Callback<User>() {
            @Override
            public void onResponse(User user) {
                if (user != null && isAdded()) {
                    fullUser = user;
                    profileName.setText(user.getName());
                    
                    String job = user.getJob() != null ? user.getJob() : "Add Job";
                    String school = user.getSchool() != null ? user.getSchool() : "Add School";
                    profileJobSchool.setText(job + " | " + school);

                    if (user.getBio() != null && !user.getBio().isEmpty()) {
                        profileBio.setText(user.getBio());
                        profileBio.setAlpha(1.0f);
                    } else {
                        profileBio.setText("Tell us about yourself...");
                        profileBio.setAlpha(0.5f);
                    }

                    // Update Profile Strength
                    int strength = (int) user.calculateProfileCompleteness();
                    profileStrengthText.setText(strength + "%");
                    profileStrengthProgress.setProgress(strength);

                    setupLifestyleBadges(user);
                    setupPrompts(user);

                    lifestyleTags.removeAllViews();
                    if (user.getLifestyleTags() != null) {
                        for (String tag : user.getLifestyleTags()) {
                            Chip chip = new Chip(getContext());
                            chip.setText(tag);
                            // Apply consistent high-contrast styling
                            chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(getContext().getColor(R.color.m3_secondaryContainer)));
                            chip.setTextColor(getContext().getColor(R.color.m3_onSecondaryContainer));
                            chip.setChipStrokeWidth(0f);
                            lifestyleTags.addView(chip);
                        }
                    }

                    // Load Preferences
                    ageRangeSlider.setValues((float)user.getMinAgePreference(), (float)user.getMaxAgePreference());
                    distanceSlider.setValue((float)user.getDistancePreference());

                    if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().equals("default")) {
                        Glide.with(ProfileFragment.this).load(user.getProfileImageUrl()).into(profileImage);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                // Handle error
            }
        });
    }
}
