package com.example.lako;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Profile_My_Shop_Facial_Recognition extends AppCompatActivity {

    private ImageView getImageIdLako, imageLako;
    private Button buttonCameraId, buttonCameraSelfie, continueButton;

    private boolean isIdUploaded = true, isSelfieUploaded = true;
    private String idImageUrl, selfieImageUrl;
    private String sellerName; // Seller's name to be included in the file name

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_my_shop_facial_recognition);

        // Retrieve the seller's name from the intent
        sellerName = getIntent().getStringExtra("sellerName");
        if (sellerName == null || sellerName.isEmpty()) {
            sellerName = "UnknownSeller"; // Default if seller's name is not provided
        }

        // Initialize views
        getImageIdLako = findViewById(R.id.image_id_lako);
        imageLako = findViewById(R.id.image_lako);
        buttonCameraId = findViewById(R.id.camera);
        buttonCameraSelfie = findViewById(R.id.upload_photo);
        continueButton = findViewById(R.id.continue_button);

        // Set click listeners
        buttonCameraId.setOnClickListener(v -> openCameraForID());
        buttonCameraSelfie.setOnClickListener(v -> openCameraForSelfie());
        continueButton.setOnClickListener(this::continueBtn);

        // Restore state if activity restarts
        if (savedInstanceState != null) {
            isIdUploaded = savedInstanceState.getBoolean("isIdUploaded", false);
            isSelfieUploaded = savedInstanceState.getBoolean("isSelfieUploaded", false);
            checkUploadCompletion();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isIdUploaded", isIdUploaded);
        outState.putBoolean("isSelfieUploaded", isSelfieUploaded);
    }

    private void openCameraForID() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncherForID.launch(intent);
    }

    private void openCameraForSelfie() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncherForSelfie.launch(intent);
    }

    private final ActivityResultLauncher<Intent> cameraLauncherForID = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> handleImageResult(result, "Verification_ID", getImageIdLako));

    private final ActivityResultLauncher<Intent> cameraLauncherForSelfie = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> handleImageResult(result, "Verification_Realtime_Selfie", imageLako));

    private void handleImageResult(ActivityResult result, String folderName, ImageView imageView) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
            if (bitmap != null) {
                Bitmap resizedBitmap = resizeBitmap(bitmap, 1024, 1024);
                imageView.setImageBitmap(resizedBitmap);
                uploadImageToFirebase(resizedBitmap, folderName, url -> {
                    if (folderName.equals("ID")) {
                        idImageUrl = url;
                        isIdUploaded = true;
                    } else if (folderName.equals("Selfie")) {
                        selfieImageUrl = url;
                        isSelfieUploaded = true;
                    }
                    checkUploadCompletion();
                });
            } else {
                Uri imageUri = result.getData().getData();
                if (imageUri != null) {
                    try {
                        Bitmap uriBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        Bitmap resizedBitmap = resizeBitmap(uriBitmap, 1024, 1024);
                        imageView.setImageBitmap(resizedBitmap);
                        uploadImageToFirebase(resizedBitmap, folderName, url -> {
                            if (folderName.equals("ID")) {
                                idImageUrl = url;
                                isIdUploaded = true;
                            } else if (folderName.equals("Selfie")) {
                                selfieImageUrl = url;
                                isSelfieUploaded = true;
                            }
                            checkUploadCompletion();
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("Debug", "Failed to load image from URI", e);
                    }
                }
            }
        } else {
            Log.e("Debug", "Failed to capture " + folderName + " image");
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxWidth;
            height = (int) (maxWidth / bitmapRatio);
        } else {
            height = maxHeight;
            width = (int) (maxHeight * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    private void uploadImageToFirebase(Bitmap bitmap, String folderName, OnImageUploadCompleteListener listener) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();

        // Use the seller's name in the filename
        String fileName = folderName + "/" + sellerName + "_" + UUID.randomUUID().toString() + ".jpg";
        StorageReference storageRef = storage.getReference().child(fileName);

        storageRef.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> listener.onComplete(uri.toString()))
                        .addOnFailureListener(e -> Log.e("Debug", "Failed to get download URL", e)))
                .addOnFailureListener(e -> Log.e("Debug", "Failed to upload image", e));
    }

    private void checkUploadCompletion() {
        if (isIdUploaded && isSelfieUploaded) {
            continueButton.setEnabled(true);
        } else {
            continueButton.setEnabled(false);
        }
    }

    public void continueBtn(View view) {
        if (isIdUploaded && isSelfieUploaded) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("Shops").child(currentUser.getUid());

                // Update Firebase with seller account information
                shopRef.child("sellerAccount").setValue(true).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseUpdate", "Seller account successfully created.");
                        Toast.makeText(this, "Seller account setup completed!", Toast.LENGTH_SHORT).show();

                        // Navigate to the loading screen
                        Intent intent = new Intent(this, Profile_My_Shop_Loading_Screen.class);
                        startActivity(intent);
                        finish(); // End current activity
                    } else {
                        Log.e("FirebaseUpdate", "Failed to update seller account.", task.getException());
                        Toast.makeText(this, "Failed to set up seller account. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "User not logged in. Please log in to continue.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please upload both ID and selfie before proceeding.", Toast.LENGTH_SHORT).show();
        }
    }

    private interface OnImageUploadCompleteListener {
        void onComplete(String url);
    }
}
