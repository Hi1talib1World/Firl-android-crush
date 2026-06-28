package com.Denzo.firl;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Denzo.firl.Model.User;
import com.Denzo.firl.Model.UserRepository;
import com.Denzo.firl.Model.UserRepositoryProvider;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private TextView emailText, phoneText;
    private UserRepository userRepository;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        emailText = findViewById(R.id.settings_email);
        phoneText = findViewById(R.id.settings_phone);

        MaterialToolbar toolbar = findViewById(R.id.settings_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        userRepository = UserRepositoryProvider.get();
        userId = FirebaseAuth.getInstance().getCurrentUser() != null ? 
                 FirebaseAuth.getInstance().getCurrentUser().getUid() : "mock_uid";

        findViewById(R.id.delete_account_btn).setOnClickListener(v -> 
            Toast.makeText(this, "Please contact support to delete your account.", Toast.LENGTH_LONG).show()
        );

        loadUserData();
    }

    private void loadUserData() {
        userRepository.getUser(userId, new UserRepository.Callback<User>() {
            @Override
            public void onResponse(User user) {
                if (user != null) {
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
