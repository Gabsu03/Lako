package com.example.lako;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Password_reset_link extends AppCompatActivity {

    private String userEmail;
    private FirebaseAuth auth;
    private Button proceedButton;
    private TextView resendText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_reset_link);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Retrieve email from intent
        userEmail = getIntent().getStringExtra("email");

        // Initialize views
        proceedButton = findViewById(R.id.proceed_reset_pass);
        resendText = findViewById(R.id.resend_link);

        // Set click listeners proceedButton.setOnClickListener(v -> proceedToLogin());
        resendText.setOnClickListener(v -> resendLink());
    }

    private void resendLink() {
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "No email available to resend link.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Resend the reset link
        auth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Reset link resent to your email.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to resend link: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
