package com.Denzo.firl.Profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.Denzo.firl.Model.User;
import com.Denzo.firl.Model.UserRepository;
import com.Denzo.firl.Model.UserRepositoryProvider;
import com.Denzo.firl.R;
import com.Denzo.firl.Utils.Constants;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView imageView1, imageView2, imageView3;
    private EditText aboutEditText, jobEditText, schoolEditText;
    private RadioGroup genderGroup;
    private RadioButton radioMan, radioWoman;
    private ChipGroup interestsChipGroup, goalChipGroup;
    private LinearLayout promptsContainer;
    private AutoCompleteTextView zodiacDropdown, smokingDropdown, drinkingDropdown;
    private View saveBtn, loadingOverlay, addPromptBtn;
    
    private UserRepository userRepository;
    private String currentUid;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userRepository = UserRepositoryProvider.get();
        currentUid = FirebaseAuth.getInstance().getCurrentUser() != null ? 
                     FirebaseAuth.getInstance().getCurrentUser().getUid() : "mock_uid";

        initViews();
        loadUserData();
    }

    private void initViews() {
        imageView1 = findViewById(R.id.image_view_1);
        imageView2 = findViewById(R.id.image_view_2);
        imageView3 = findViewById(R.id.image_view_3);

        aboutEditText = findViewById(R.id.about_me);
        jobEditText = findViewById(R.id.job_title);
        schoolEditText = findViewById(R.id.school);

        genderGroup = findViewById(R.id.edit_gender_group);
        radioMan = findViewById(R.id.radio_man);
        radioWoman = findViewById(R.id.radio_woman);
        
        interestsChipGroup = findViewById(R.id.edit_interests_chip_group);
        goalChipGroup = findViewById(R.id.goal_chip_group);
        promptsContainer = findViewById(R.id.edit_prompts_container);
        
        zodiacDropdown = findViewById(R.id.zodiac_dropdown);
        smokingDropdown = findViewById(R.id.smoking_dropdown);
        drinkingDropdown = findViewById(R.id.drinking_dropdown);

        saveBtn = findViewById(R.id.save_profile_btn);
        addPromptBtn = findViewById(R.id.add_prompt_btn);
        loadingOverlay = findViewById(R.id.edit_loading_overlay);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.edit_profile_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        
        saveBtn.setOnClickListener(v -> saveProfile());
        addPromptBtn.setOnClickListener(v -> showAddPromptDialog());
    }

    private void loadUserData() {
        setLoading(true);
        userRepository.getUser(currentUid, new UserRepository.Callback<User>() {
            @Override
            public void onResponse(User user) {
                setLoading(false);
                if (user != null) {
                    currentUser = user;
                    populateFields();
                }
            }

            @Override
            public void onError(Exception e) {
                setLoading(false);
                Toast.makeText(EditProfileActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateFields() {
        if (currentUser.getBio() != null) aboutEditText.setText(currentUser.getBio());
        if (currentUser.getJob() != null) jobEditText.setText(currentUser.getJob());
        if (currentUser.getSchool() != null) schoolEditText.setText(currentUser.getSchool());

        setupDropdowns();
        setupGoalChips();
        setupEditPrompts();

        if ("Male".equalsIgnoreCase(currentUser.getSex())) {
            radioMan.setChecked(true);
        } else if ("Female".equalsIgnoreCase(currentUser.getSex())) {
            radioWoman.setChecked(true);
        }

        if (currentUser.getProfileImageUrl() != null && !currentUser.getProfileImageUrl().equals("default")) {
            Glide.with(this).load(currentUser.getProfileImageUrl()).into(imageView1);
        }

        interestsChipGroup.removeAllViews();
        List<String> userInterests = currentUser.getInterests() != null ? currentUser.getInterests() : new ArrayList<>();
        
        for (String interest : Constants.INTERESTS) {
            Chip chip = new Chip(this);
            chip.setText(interest);
            chip.setCheckable(true);
            
            // Apply consistent styling
            chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(0x20FFFFFF));
            chip.setTextColor(getColor(android.R.color.white));
            chip.setChipStrokeColor(android.content.res.ColorStateList.valueOf(0x40FFFFFF));
            chip.setChipStrokeWidth(2f);
            
            if (userInterests.contains(interest)) {
                chip.setChecked(true);
                chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(getColor(R.color.colorAccent)));
                chip.setTextColor(getColor(R.color.bg1main));
            }

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(getColor(R.color.colorAccent)));
                    chip.setTextColor(getColor(R.color.bg1main));
                } else {
                    chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(0x20FFFFFF));
                    chip.setTextColor(getColor(android.R.color.white));
                }
            });

            interestsChipGroup.addView(chip);
        }
    }

    private void setupEditPrompts() {
        promptsContainer.removeAllViews();
        if (currentUser.getPrompts() != null) {
            for (java.util.Map.Entry<String, String> entry : currentUser.getPrompts().entrySet()) {
                addPromptView(entry.getKey(), entry.getValue());
            }
        }
    }

    private void addPromptView(String question, String answer) {
        View view = getLayoutInflater().inflate(R.layout.item_edit_prompt, promptsContainer, false);
        TextView qText = view.findViewById(R.id.edit_prompt_question);
        EditText aEdit = view.findViewById(R.id.edit_prompt_answer);
        View deleteBtn = view.findViewById(R.id.delete_prompt);

        qText.setText(question);
        aEdit.setText(answer);
        deleteBtn.setOnClickListener(v -> promptsContainer.removeView(view));
        
        promptsContainer.addView(view);
    }

    private void showAddPromptDialog() {
        String[] questions = {
            "My dream date is...",
            "The most adventurous thing I've done...",
            "A fun fact about me...",
            "I'm looking for someone who...",
            "My favorite weekend activity..."
        };
        
        new androidx.appcompat.app.AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
            .setTitle("Pick a Prompt")
            .setItems(questions, (dialog, which) -> {
                addPromptView(questions[which], "");
            }).show();
    }

    private void setupDropdowns() {
        String[] zodiacs = {"Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"};
        String[] smokingOptions = {"Never", "Socially", "Regularly"};
        String[] drinkingOptions = {"Never", "Socially", "Regularly"};

        setupAdapter(zodiacDropdown, zodiacs, currentUser.getZodiac());
        setupAdapter(smokingDropdown, smokingOptions, currentUser.getSmoking());
        setupAdapter(drinkingDropdown, drinkingOptions, currentUser.getDrinking());
    }

    private void setupAdapter(AutoCompleteTextView view, String[] options, String current) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_dropdown, options);
        view.setAdapter(adapter);
        if (current != null) view.setText(current, false);
    }

    private void setupGoalChips() {
        String[] goals = {"Long Term", "Short Term", "New Friends", "Not Sure"};
        goalChipGroup.removeAllViews();
        for (String goal : goals) {
            Chip chip = new Chip(this);
            chip.setText(goal);
            chip.setCheckable(true);
            
            // Apply consistent styling
            chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(0x20FFFFFF));
            chip.setTextColor(getColor(android.R.color.white));
            
            if (goal.equals(currentUser.getRelationshipGoal())) {
                chip.setChecked(true);
                chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(getColor(R.color.colorAccent)));
                chip.setTextColor(getColor(R.color.bg1main));
            }

            chip.setOnCheckedChangeListener((bv, isChecked) -> {
                if (isChecked) {
                    chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(getColor(R.color.colorAccent)));
                    chip.setTextColor(getColor(R.color.bg1main));
                } else {
                    chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(0x20FFFFFF));
                    chip.setTextColor(getColor(android.R.color.white));
                }
            });
            goalChipGroup.addView(chip);
        }
    }

    private void saveProfile() {
        if (currentUser == null) return;

        currentUser.setBio(aboutEditText.getText().toString());
        currentUser.setJob(jobEditText.getText().toString());
        currentUser.setSchool(schoolEditText.getText().toString());
        
        currentUser.setZodiac(zodiacDropdown.getText().toString());
        currentUser.setSmoking(smokingDropdown.getText().toString());
        currentUser.setDrinking(drinkingDropdown.getText().toString());

        if (radioMan.isChecked()) currentUser.setSex("Male");
        else if (radioWoman.isChecked()) currentUser.setSex("Female");

        for (int i = 0; i < goalChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) goalChipGroup.getChildAt(i);
            if (chip.isChecked()) {
                currentUser.setRelationshipGoal(chip.getText().toString());
                break;
            }
        }

        List<String> selectedInterests = new ArrayList<>();
        for (int i = 0; i < interestsChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) interestsChipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selectedInterests.add(chip.getText().toString());
            }
        }
        currentUser.setInterests(selectedInterests);

        java.util.Map<String, String> updatedPrompts = new java.util.HashMap<>();
        for (int i = 0; i < promptsContainer.getChildCount(); i++) {
            View view = promptsContainer.getChildAt(i);
            TextView qText = view.findViewById(R.id.edit_prompt_question);
            EditText aEdit = view.findViewById(R.id.edit_prompt_answer);
            String q = qText.getText().toString();
            String a = aEdit.getText().toString();
            if (!a.isEmpty()) {
                updatedPrompts.put(q, a);
            }
        }
        currentUser.setPrompts(updatedPrompts);

        setLoading(true);
        userRepository.updateProfile(currentUser, new UserRepository.Callback<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                setLoading(false);
                Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception e) {
                setLoading(false);
                Toast.makeText(EditProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean loading) {
        loadingOverlay.setVisibility(loading ? View.VISIBLE : View.GONE);
        saveBtn.setEnabled(!loading);
    }
}
