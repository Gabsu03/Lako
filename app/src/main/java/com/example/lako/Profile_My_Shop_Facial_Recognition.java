package com.example.lako;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;



public class Profile_My_Shop_Facial_Recognition extends AppCompatActivity {

    ImageView getImageIdLako; // For ID ImageView
    ImageView imageLako; // For Selfie ImageView

    Button buttonCameraId, buttonCameraSelfie;

    boolean isIdUploaded = false; // Track if ID image is uploaded
    boolean isSelfieUploaded = false; // Track if selfie image is uploaded

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_my_shop_facial_recognition);

        // Initialize ImageViews and Buttons
        getImageIdLako = findViewById(R.id.image_id_lako); // ID ImageView
        imageLako = findViewById(R.id.image_lako); // Selfie ImageView
        buttonCameraId = findViewById(R.id.camera); // Button for ID upload
        buttonCameraSelfie = findViewById(R.id.upload_photo); // Button for Selfie

        // Button click listeners
        buttonCameraId.setOnClickListener(v -> openCameraForID());
        buttonCameraSelfie.setOnClickListener(v -> openCameraForSelfie());
    }

    private void openCameraForID() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncherForID.launch(intent);
    }

    private void openCameraForSelfie() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncherForSelfie.launch(intent);
    }

    // Launcher for ID upload
    private final ActivityResultLauncher<Intent> cameraLauncherForID = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    if (bundle != null) {
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        if (bitmap != null) {
                            getImageIdLako.setImageBitmap(bitmap); // Set to ID ImageView
                            isIdUploaded = true; // Mark ID as uploaded
                        }
                    }
                }
            }
    );

    // Launcher for Selfie upload
    private final ActivityResultLauncher<Intent> cameraLauncherForSelfie = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    if (bundle != null) {
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        if (bitmap != null) {
                            imageLako.setImageBitmap(bitmap); // Set to Selfie ImageView
                            isSelfieUploaded = true; // Mark Selfie as uploaded
                        }
                    }
                }
            }
    );

    // Method for continue button
    public void continueBtn(View view) {
        if (isIdUploaded && isSelfieUploaded) {
            // Both images are uploaded, proceed to next activity
            Intent intent = new Intent(this, Profile_My_Shop_Loading_Screen.class);
            startActivity(intent);
        } else {
            // Show a message if either image is missing
            Toast.makeText(this, "Please upload both ID and selfie before proceeding.", Toast.LENGTH_SHORT).show();
        }
    }
}



