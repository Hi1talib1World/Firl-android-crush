package com.Denzo.firl;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.materialswitch.MaterialSwitch;

import androidx.appcompat.app.AppCompatActivity;

import com.Denzo.firl.Model.User;
import com.Denzo.firl.Model.UserRepository;
import com.Denzo.firl.Model.UserRepositoryProvider;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private TextView emailText, phoneText;
    private MaterialSwitch incognitoSwitch, showProfileSwitch;
    private UserRepository userRepository;
    private String userId;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        emailText = findViewById(R.id.settings_email);
        phoneText = findViewById(R.id.settings_phone);
        incognitoSwitch = findViewById(R.id.switch_incognito);
        showProfileSwitch = findViewById(R.id.switch_show_profile);

        MaterialToolbar toolbar = findViewById(R.id.settings_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        userRepository = UserRepositoryProvider.get();
        userId = FirebaseAuth.getInstance().getCurrentUser() != null ? 
                 FirebaseAuth.getInstance().getCurrentUser().getUid() : "mock_uid";

        findViewById(R.id.delete_account_btn).setOnClickListener(v -> 
            showDeleteAccountConfirmation()
        );

        setupSwitchListeners();
        loadUserData();
    }

    private void setupSwitchListeners() {
        incognitoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentUser != null) {
                // In real app, save to DB. For mock, just toast.
                Toast.makeText(this, "Incognito " + (isChecked ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
            }
        });

        showProfileSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentUser != null) {
                Toast.makeText(this, "Profile visibility updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteAccountConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
            .setTitle("Delete Account")
            .setMessage("This action is permanent and will delete all your data. Are you sure?")
            .setPositiveButton("Delete Forever", (d, w) -> {
                // Simulate deletion
                Toast.makeText(this, "Account scheduled for deletion.", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                android.content.Intent intent = new android.content.Intent(this, WelcomeActivity.class);
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void loadUserData() {
        userRepository.getUser(userId, new UserRepository.Callback<User>() {
            @Override
            public void onResponse(User user) {
                if (user != null) {
                    currentUser = user;
                    if (user.getEmail() != null) emailText.setText(user.getEmail());
                    if (user.getPhone() != null) phoneText.setText(user.getPhone());
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SettingsActivity.this, "Error loading account data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
