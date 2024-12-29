package com.example.lako;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Main_Shop_Seller_Edit extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    private Uri imageUri;
    private ShapeableImageView profileImageView;
    private Button uploadButton, saveButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference shopsRef;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop_seller_edit);

        // Initialize views and Firebase instances
        profileImageView = findViewById(R.id.profile_picture_shop);
        uploadButton = findViewById(R.id.upload_photoo_btn);
        saveButton = findViewById(R.id.save_btn_profile_seller);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        shopsRef = firebaseDatabase.getReference("shops");

        // Load existing profile image
        loadProfileImage();

        // Set up upload button to allow gallery or camera selection
        uploadButton.setOnClickListener(v -> openGalleryOrCamera());
    }

    // Load existing profile image from Firebase using Glide
    private void loadProfileImage() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        shopsRef.child(userId).child("profileImageUrl").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String profileImageUrl = task.getResult().getValue(String.class);
                if (profileImageUrl != null) {
                    // Use Glide to load the image into the ImageView
                    Glide.with(Main_Shop_Seller_Edit.this)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.image_upload)  // Placeholder image
                            .error(R.drawable.image_upload)  // Error image
                            .into(profileImageView);
                }
            } else {
                Toast.makeText(Main_Shop_Seller_Edit.this, "Error loading profile image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Open gallery or camera for selecting an image
    private void openGalleryOrCamera() {
        CharSequence options[] = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Upload Profile Photo");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhotoIntent, GALLERY_REQUEST_CODE);
            } else {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                imageUri = data.getData(); // Get image URI from gallery
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                imageUri = data.getData(); // Get image URI from camera
            }
            uploadProfilePicture();
        }
    }

    // Upload the profile picture to Firebase Storage and save URL in the database
    // Upload the profile picture to Firebase Storage and save URL in the database
    private void uploadProfilePicture() {
        if (imageUri != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            StorageReference fileReference = storageReference.child("profile_pictures_seller_shop/" + userId + ".jpg");

            fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    // Save image URL in Firebase Database
                    shopsRef.child(userId).child("profileImageUrl").setValue(imageUrl).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Update the ImageView with the new image using Glide
                            Glide.with(Main_Shop_Seller_Edit.this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.image_upload)  // Placeholder image
                                    .error(R.drawable.image_upload)  // Error image
                                    .centerCrop()
                                    .into(profileImageView);
                            Toast.makeText(Main_Shop_Seller_Edit.this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Main_Shop_Seller_Edit.this, "Failed to save image URL in Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(Main_Shop_Seller_Edit.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            });
        }
    }

    // Save the profile picture information and proceed
    public void save_button_edit_profile_seller(View view) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        shopsRef.child(userId).child("profileImageUrl").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String profileImageUrl = task.getResult().getValue(String.class);

                // Navigate to the loading screen first
                Intent intent = new Intent(Main_Shop_Seller_Edit.this, Main_Shop_Seller_Edit_Profile_Loading_Screen.class);
                startActivity(intent);

                // After loading, send the updated profile image URL to Main_Shop_Seller_Products
                new Handler().postDelayed(() -> {
                    Intent nextIntent = new Intent(Main_Shop_Seller_Edit.this, Main_Shop_Seller_Products.class);
                    nextIntent.putExtra("profileImageUrl", profileImageUrl);
                    startActivity(nextIntent);
                }, 2000); // Delay before navigating
            } else {
                Toast.makeText(Main_Shop_Seller_Edit.this, "Error loading profile image URL", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

