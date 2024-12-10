package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_Settings_information extends AppCompatActivity {

    private TextView lastnameTextView;
    private TextView firstnameTextView;
    private TextView usernameTextView;

    private TextView usernamedisplay;
    private TextView emailTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private static final int EDIT_PROFILE_REQUEST_CODE = 1; // Define a request code
    private String currentUsername;  // Store the current username locally

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings_information);

        lastnameTextView = findViewById(R.id.lastname_information);
        firstnameTextView = findViewById(R.id.firstname_information);
        usernamedisplay = findViewById(R.id.username_information);
        usernameTextView = findViewById(R.id.username_info); // Ensure correct ID reference
        emailTextView = findViewById(R.id.email_information);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Fetch user data initially
        fetchUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Fetch user data when returning to the activity
        fetchUserData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Retrieve the updated data from the Intent
            String updatedFirstName = data.getStringExtra("updatedFirstName");
            String updatedLastName = data.getStringExtra("updatedLastName");
            String updatedUsername = data.getStringExtra("updatedUsername"); // Fetch updated username

            // Save the updated username
            if (updatedUsername != null) {
                currentUsername = updatedUsername;  // Update the local variable
            }

            // Update the TextViews with the updated data
            if (updatedFirstName != null) {
                firstnameTextView.setText(updatedFirstName);
            }
            if (updatedLastName != null) {
                lastnameTextView.setText(updatedLastName);
            }
            if (updatedUsername != null) {
                usernameTextView.setText(updatedUsername); // Update username display
                usernamedisplay.setText(updatedUsername); // Update username display
            }

            // Ensure the username is saved to Firebase
            saveUsernameToFirebase(updatedUsername);
        }
    }


    private void saveUsernameToFirebase(String updatedUsername) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child(userId).child("username").setValue(updatedUsername)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Profile_Settings_information.this, "Username updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Profile_Settings_information.this, "Error updating username", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchUserData() {
        // Get the current user ID
        String userId = mAuth.getCurrentUser().getUid();

        mDatabase.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot dataSnapshot = task.getResult();
                String firstName = dataSnapshot.child("firstName").getValue(String.class);
                String lastName = dataSnapshot.child("lastName").getValue(String.class);
                String username = dataSnapshot.child("username").getValue(String.class); // Fetch username
                String email = mAuth.getCurrentUser().getEmail(); // Get email from FirebaseAuth

                // Update TextViews with fetched data
                firstnameTextView.setText(firstName != null ? firstName : "N/A");
                lastnameTextView.setText(lastName != null ? lastName : "N/A");
                usernameTextView.setText(username != null ? username : "N/A"); // Display username
                usernamedisplay.setText(username != null ? username : "N/A"); // Display username
                emailTextView.setText(email != null ? email : "N/A");

                // Set the currentUsername to the latest value from Firebase
                if (username != null) {
                    currentUsername = username;
                }
            } else {
                Toast.makeText(Profile_Settings_information.this, "Error loading profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void edit_profile(View view) {
        // Start the Profile_Edit activity and wait for the result
        Intent intent = new Intent(Profile_Settings_information.this, Profile_Edit.class);
        startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE);
    }

    public void profile_back(View view) {
        // Navigate back to the previous activity
        finish(); // Close the current activity
    }
}








