package com.Denzo.firl.Login.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Denzo.firl.Login.LoginActivity;
import com.Denzo.firl.MainActivity;
import com.Denzo.firl.Model.User;
import com.Denzo.firl.Model.UserRepository;
import com.Denzo.firl.Model.UserRepositoryProvider;
import com.Denzo.firl.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText emailInput, nameInput, passwordInput;
    private RadioGroup genderGroup;
    private MaterialButton registerBtn;
    private View loadingOverlay;
    private TextView loginLink;
    private UserRepository userRepository;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userRepository = UserRepositoryProvider.get();

        emailInput = findViewById(R.id.remail);
        nameInput = findViewById(R.id.rname);
        passwordInput = findViewById(R.id.rpassword);
        genderGroup = findViewById(R.id.radioGroup);
        registerBtn = findViewById(R.id.rbutton);
        loadingOverlay = findViewById(R.id.loading_overlay);
        
        findViewById(R.id.register_back_btn).setOnClickListener(v -> finish());

        registerBtn.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        if (isLoading) return;

        String email = emailInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        
        int selectId = genderGroup.getCheckedRadioButtonId();
        if (selectId == -1) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton radioButton = findViewById(selectId);
        String gender = radioButton.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        setLoading(true);

        User newUser = new User();
        newUser.setUid("mock_new_uid"); // In real app, this comes from Auth
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setSex(gender);
        newUser.setProfileImageUrl("default");

        userRepository.createUser(newUser, new UserRepository.Callback<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                setLoading(false);
                if (response) {
                    Toast.makeText(SignupActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(Exception e) {
                setLoading(false);
                Toast.makeText(SignupActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(boolean loading) {
        this.isLoading = loading;
        loadingOverlay.setVisibility(loading ? View.VISIBLE : View.GONE);
        registerBtn.setEnabled(!loading);
    }
}
