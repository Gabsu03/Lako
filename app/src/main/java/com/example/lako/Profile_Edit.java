package com.example.lako;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Profile_Edit extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText usernameEditText;
    private EditText emailEditText;
    private TextView displayName;
    private Button saveButton;
    private ProgressBar progressBar;
    private ImageView uploadImage;
    private Uri imageUri;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        firstNameEditText = findViewById(R.id.first_name_edit);
        lastNameEditText = findViewById(R.id.Last_name_edit);
        usernameEditText = findViewById(R.id.username_edit);
        emailEditText = findViewById(R.id.email_edit);
        displayName = findViewById(R.id.display_name);
        saveButton = findViewById(R.id.save_edit_profile);
        progressBar = findViewById(R.id.progress_bar_edit_profile);
        uploadImage = findViewById(R.id.UploadImage);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mStorage = FirebaseStorage.getInstance().getReference("profile_images");

        emailEditText.setEnabled(false);
        fetchUserData();

        saveButton.setEnabled(false);

        firstNameEditText.addTextChangedListener(inputWatcher);
        lastNameEditText.addTextChangedListener(inputWatcher);

        saveButton.setOnClickListener(view -> saveProfileChanges());

        Button imageUploadButton = findViewById(R.id.image_upload_btn);
        imageUploadButton.setOnClickListener(v -> openImageSelector());
    }

    private TextWatcher inputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            saveButton.setEnabled(!firstName.isEmpty() && !lastName.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private void fetchUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            mDatabase.child(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = user.getEmail();

                    firstNameEditText.setText(firstName);
                    lastNameEditText.setText(lastName);
                    emailEditText.setText(email);
                    usernameEditText.setText(username);
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
        if (username != null && !username.isEmpty()) {
            displayName.setText(username);
        } else {
            displayName.setText(firstName + " " + lastName);
        }
    }

    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            uploadImage.setImageURI(imageUri);
        }
    }

    private void saveProfileChanges() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        saveButton.setEnabled(false);

        mDatabase.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                    progressBar.setVisibility(View.GONE);
                    saveButton.setEnabled(true);
                    Toast.makeText(Profile_Edit.this, "Username already exists. Please choose a different username.", Toast.LENGTH_SHORT).show();
                } else {
                    saveOrUpdateUserProfile(firstName, lastName, username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                saveButton.setEnabled(true);
                Toast.makeText(Profile_Edit.this, "Error checking username availability", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveOrUpdateUserProfile(String firstName, String lastName, String username) {
        String userId = mAuth.getCurrentUser().getUid();

        if (imageUri != null) {
            String imageName = firstName + "_" + lastName + ".jpg";
            StorageReference fileReference = mStorage.child(imageName);

            fileReference.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    fileReference.getDownloadUrl().addOnCompleteListener(urlTask -> {
                        if (urlTask.isSuccessful()) {
                            String imageUrl = urlTask.getResult().toString();
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
            updateProfileData(firstName, lastName, username, null);
        }
    }

    private void updateProfileData(String firstName, String lastName, String username, String imageUrl) {
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("firstName", firstName);
        userUpdates.put("lastName", lastName);
        userUpdates.put("username", username);
        if (imageUrl != null) {
            userUpdates.put("profileImage", imageUrl);
        }

        mDatabase.child(userId).updateChildren(userUpdates).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            saveButton.setEnabled(true);

            if (task.isSuccessful()) {
                Toast.makeText(Profile_Edit.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                updateDisplayName(firstName, lastName, username);
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(Profile_Edit.this, "Error updating profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
