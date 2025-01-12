package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_Settings_Add_MFA extends AppCompatActivity {

    private EditText secondaryEmailInput;
    private TextView savedEmailDisplay;

    // Firebase Database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_settings_add_mfa);

        secondaryEmailInput = findViewById(R.id.secondary_email_input);
        Button saveEmailButton = findViewById(R.id.save_email_button);
        savedEmailDisplay = findViewById(R.id.saved_email_display);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Multi-Factor").child("user_id");

        // Load existing email from Firebase
        databaseReference.child("secondaryEmail").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getValue() != null) {
                String savedEmail = task.getResult().getValue(String.class);
                savedEmailDisplay.setText(savedEmail);
            } else {
                savedEmailDisplay.setText("No secondary email set");
            }
        });

        // Save email to Firebase
        saveEmailButton.setOnClickListener(view -> {
            String email = secondaryEmailInput.getText().toString().trim();

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save to Firebase
            databaseReference.child("secondaryEmail").setValue(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    savedEmailDisplay.setText(email);
                    Toast.makeText(this, "Secondary email saved successfully", Toast.LENGTH_SHORT).show();
                    secondaryEmailInput.setText("");
                } else {
                    Toast.makeText(this, "Failed to save email. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Navigate back to MFA settings
    public void drop_up_mfa(View view) {
        Intent intent = new Intent(Profile_Settings_Add_MFA.this, Profile_Settings_MFA.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
