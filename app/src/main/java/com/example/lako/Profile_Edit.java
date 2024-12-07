package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Profile_Edit extends AppCompatActivity {

    private TextInputEditText usernameEdit, lastNameEdit, firstNameEdit, emailEdit;
    private Button saveProfileButton, uploadImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_edit);

        // Initialize views
        usernameEdit = findViewById(R.id.username_edit);
        lastNameEdit = findViewById(R.id.Last_name_edit);
        firstNameEdit = findViewById(R.id.first_name_edit);
        emailEdit = findViewById(R.id.email_edit);
        saveProfileButton = findViewById(R.id.save_edit_profile);
        uploadImageButton = findViewById(R.id.image_upload_btn);

        // Set up button click listeners
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges();
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void saveProfileChanges() {
        String username = usernameEdit.getText().toString().trim();
        String lastName = lastNameEdit.getText().toString().trim();
        String firstName = firstNameEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();

        if (username.isEmpty() || lastName.isEmpty() || firstName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulate saving profile changes
        Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();

        // Navigate back or refresh the activity as needed
    }

    private void uploadImage() {
        // Simulate image upload (expand logic based on actual implementation)
        Toast.makeText(this, "Image upload clicked!", Toast.LENGTH_SHORT).show();

        // Open an image picker or camera intent here if needed
    }
}
