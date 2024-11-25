package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password_Email extends AppCompatActivity {

    private EditText emailInput;
    private Button reset_email_btn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_email);

        // Initialize views
        emailInput = findViewById(R.id.email_input);
        reset_email_btn = findViewById(R.id.reset_email_btn);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Set button click listener
        reset_email_btn.setOnClickListener(v -> sendResetLink());
    }

    public void Back_Login_email(View view) {
        startActivity(new Intent(Forgot_Password_Email.this, sign_in.class));
    }

    public void security_question(View view) {
        startActivity(new Intent(Forgot_Password_Email.this, Security_Questions_reset_pass.class));
    }

    private void sendResetLink() {
        String email = emailInput.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password reset link sent. Please check your email.", Toast.LENGTH_SHORT).show();
                        // Redirect to the reset link screen
                        Intent intent = new Intent(Forgot_Password_Email.this, Password_reset_link.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Failed to send reset link: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}