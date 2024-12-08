package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Profile_Edit extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Button saveButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_edit);

        firstNameEditText = findViewById(R.id.first_name_edit);
        lastNameEditText = findViewById(R.id.Last_name_edit);
        saveButton = findViewById(R.id.save_edit_profile);

        saveButton.setOnClickListener(view -> saveProfileChanges());
    }

    private void saveProfileChanges() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulate saving changes (replace this with actual data handling code)
        progressBar.setVisibility(View.VISIBLE);
        saveButton.setEnabled(false);

        // Here, you can pass the updated name to the other fragment or activity.
        Intent intent = new Intent();
        intent.putExtra("updatedFirstName", firstName);
        intent.putExtra("updatedLastName", lastName);

        // Simulating a delay to represent saving
        saveButton.postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            saveButton.setEnabled(true);
            Toast.makeText(Profile_Edit.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            // Returning result to the previous activity/fragment
            setResult(RESULT_OK, intent);
            finish();
        }, 1500);
    }
}