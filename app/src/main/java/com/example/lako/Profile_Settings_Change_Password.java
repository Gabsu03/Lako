package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile_Settings_Change_Password extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button resetEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings_change_password);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Find the reset button
        resetEmailButton = findViewById(R.id.use_email_btnn);

        // Set up the button listener to send reset email and show the Toast message
        resetEmailButton.setOnClickListener(v -> sendPasswordResetEmail());
    }

    private void sendPasswordResetEmail() {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String userEmail = user.getEmail();
            Log.d("PasswordReset", "User email: " + userEmail); // Debugging log

            auth.sendPasswordResetEmail(userEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Inform the user that the reset email was sent
                            Toast.makeText(Profile_Settings_Change_Password.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                            Log.d("PasswordReset", "Password reset email sent to: " + userEmail);

                            // Do not proceed to the next activity, just show a Toast
                            // Remove the intent and finish part to prevent going to the next activity
                        } else {
                            // In case of failure, show a message to the user
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(Profile_Settings_Change_Password.this, "Failed to send reset email: " + errorMessage, Toast.LENGTH_SHORT).show();
                            Log.e("PasswordReset", "Error: " + errorMessage);
                        }
                    });
        } else {
            Log.d("PasswordReset", "No user is logged in.");
            Toast.makeText(this, "No user is currently logged in", Toast.LENGTH_SHORT).show();
        }
    }

    // Back button for Profile Settings (if necessary)
    public void Back_profile_settings(View view) {
        finish(); // Go back to previous activity
    }
}




