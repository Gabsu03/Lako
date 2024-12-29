package com.example.lako;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Profile_Edit extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText usernameEditText;
    private EditText emailEditText;  // Email field is non-editable
    private TextView displayName; // Display name TextView
    private Button saveButton;
    private ProgressBar progressBar;
    private ImageView uploadImage;
    private Uri imageUri;  // To store the selected image URI

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // Initialize UI elements
        firstNameEditText = findViewById(R.id.first_name_edit);
        lastNameEditText = findViewById(R.id.Last_name_edit);
        usernameEditText = findViewById(R.id.username_edit);
        emailEditText = findViewById(R.id.email_edit);  // Initialize email EditText
        displayName = findViewById(R.id.display_name); // Initialize display_name TextView
        saveButton = findViewById(R.id.save_edit_profile);
        progressBar = findViewById(R.id.progress_bar_edit_profile);
        uploadImage = findViewById(R.id.UploadImage);  // ImageView for profile photo

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mStorage = FirebaseStorage.getInstance().getReference("profile_images"); // Firebase Storage path for images

        // Disable the email EditText as it's non-editable
        emailEditText.setEnabled(false);

        // Fetch user data to populate the fields
        fetchUserData();

        // Set the save button to be initially disabled
        saveButton.setEnabled(false); // Disable save button initially

        // Add TextWatcher to enable save button when both fields are filled
        firstNameEditText.addTextChangedListener(inputWatcher);
        lastNameEditText.addTextChangedListener(inputWatcher);

        // Set the save button click listener
        saveButton.setOnClickListener(view -> {
            saveProfileChanges();
        });

        // Set the image upload button to open image picker
        Button imageUploadButton = findViewById(R.id.image_upload_btn);
        imageUploadButton.setOnClickListener(v -> openImageSelector());
    }

    // TextWatcher to check if both first and last name fields are filled
    private TextWatcher inputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();

            // Enable save button only if both fields are non-empty
            saveButton.setEnabled(!firstName.isEmpty() && !lastName.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    // Fetch user data and populate the form fields
    private void fetchUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            mDatabase.child(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class); // Fetch username
                    String email = mAuth.getCurrentUser().getEmail(); // Get email from FirebaseAuth

                    // Populate the EditText fields
                    firstNameEditText.setText(firstName);
                    lastNameEditText.setText(lastName);
                    emailEditText.setText(email);  // Set email in EditText (disabled, non-editable)
                    usernameEditText.setText(username); // Display username in the EditText

                    // Set the display name
                    updateDisplayName(firstName, lastName, username);
                } else {
                    Toast.makeText(Profile_Edit.this, "Error loading profile data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(Profile_Edit.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDisplayName(String firstName, String lastName, String username) {
        // If username is present, we display it as the nickname
        if (username != null && !username.isEmpty()) {
            displayName.setText(username);  // Set the display name to username
        } else {
            // Else, fallback to the full name if no username is provided
            displayName.setText(firstName + " " + lastName);  // Combine first and last name as display name
        }
    }

    // Open image picker to choose an image
    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 100); // 100 is the request code for image selection
    }

    // Handle the result of the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData(); // Get the selected image URI
            uploadImage.setImageURI(imageUri); // Set the image URI to ImageView
        }
    }

    // Save the profile changes to Firebase
    private void saveProfileChanges() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim(); // Get the editable username

        // Check if all fields are filled
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty()) {
            // Show a toast if any of the fields are empty
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;  // Don't proceed with the update
        }

        // Show the progress bar and disable the save button while saving
        progressBar.setVisibility(View.VISIBLE);
        saveButton.setEnabled(false);

        String userId = mAuth.getCurrentUser().getUid();

        // If an image is selected, upload it to Firebase Storage
        if (imageUri != null) {
            // Create the filename using first and last name (or username)
            String imageName = firstName + "_" + lastName + ".jpg"; // Example: "John_Doe.jpg"
            StorageReference fileReference = mStorage.child(imageName); // Use the generated name for the file

            fileReference.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    fileReference.getDownloadUrl().addOnCompleteListener(urlTask -> {
                        if (urlTask.isSuccessful()) {
                            String imageUrl = urlTask.getResult().toString();

                            // Update the user's profile with image URL
                            updateProfileData(firstName, lastName, username, imageUrl);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            saveButton.setEnabled(true);
                            Toast.makeText(Profile_Edit.this, "Error getting image URL", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    saveButton.setEnabled(true);
                    Toast.makeText(Profile_Edit.this, "Error uploading image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // No image selected, update without image URL
            updateProfileData(firstName, lastName, username, null);
        }
    }


    // Method to update the profile data in Firebase
    private void updateProfileData(String firstName, String lastName, String username, String imageUrl) {
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("firstName", firstName);
        userUpdates.put("lastName", lastName);
        userUpdates.put("username", username);
        if (imageUrl != null) {
            userUpdates.put("profileImage", imageUrl); // Add image URL to the update
        }

        mDatabase.child(userId).updateChildren(userUpdates).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            saveButton.setEnabled(true); // Enable save button after operation

            if (task.isSuccessful()) {
                Toast.makeText(Profile_Edit.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                updateDisplayName(firstName, lastName, username); // Update display name
                setResult(RESULT_OK);
                finish();  // Finish the activity and return to the previous one
            } else {
                Toast.makeText(Profile_Edit.this, "Error updating profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to generate a nickname based on the username
    private String generateNickname(String username) {
        // For example, the nickname could just be the first part of the username
        String[] parts = username.split("\\s+");  // Split by spaces if any
        return parts[0];  // Use the first part as the nickname
    }

    // Fetch the updated data when returning to this activity
    @Override
    protected void onStart() {
        super.onStart();
        fetchUserData();  // Fetch the latest user data from Firebase when the activity starts
    }
}










