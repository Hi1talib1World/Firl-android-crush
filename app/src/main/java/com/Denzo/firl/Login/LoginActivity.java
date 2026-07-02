package com.Denzo.firl.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Denzo.firl.Login.register.SignupActivity;
import com.Denzo.firl.MainActivity;
import com.Denzo.firl.Utils.ActivityTracker;
import com.Denzo.firl.Model.ActivityLog;
import com.Denzo.firl.Model.User;
import com.Denzo.firl.Model.UserRepository;
import com.Denzo.firl.Model.UserRepositoryProvider;
import com.Denzo.firl.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private MaterialButton loginBtn;
    private View loadingOverlay;
    private TextView signupLink, forgetPassword;
    private UserRepository userRepository;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userRepository = UserRepositoryProvider.get();

        emailInput = findViewById(R.id.lemail);
        passwordInput = findViewById(R.id.lpassword);
        loginBtn = findViewById(R.id.llogin);
        loadingOverlay = findViewById(R.id.loading_overlay);
        signupLink = findViewById(R.id.link_signup);
        forgetPassword = findViewById(R.id.forget);

        loginBtn.setOnClickListener(v -> loginUserAccount());

        signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.login_back_btn).setOnClickListener(v -> finish());

        forgetPassword.setOnClickListener(v -> 
            Toast.makeText(LoginActivity.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show()
        );
    }

    private void loginUserAccount() {
        if (isLoading) return;

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            return;
        }

        setLoading(true);

        userRepository.loginUser(email, password, new UserRepository.Callback<User>() {
            @Override
            public void onResponse(User user) {
                setLoading(false);
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "Welcome back, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                    ActivityTracker.getInstance().log("Login", ActivityLog.Status.SUCCESS, "User: " + user.getName());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(Exception e) {
                setLoading(false);
                Toast.makeText(LoginActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                ActivityTracker.getInstance().log("Login", ActivityLog.Status.FAILURE, e.getMessage());
            }
        });
    }

    private void setLoading(boolean loading) {
        this.isLoading = loading;
        loadingOverlay.setVisibility(loading ? View.VISIBLE : View.GONE);
        loginBtn.setEnabled(!loading);
    }
}
