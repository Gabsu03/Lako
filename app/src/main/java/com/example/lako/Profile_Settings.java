package com.example.lako;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Profile_Settings extends AppCompatActivity {

    private boolean isDropdownUp;
    private TextView nameInput;  // TextView for displaying the user's name
    private ShapeableImageView profileImageView; // ImageView for displaying profile image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        // Initialize views
        nameInput = findViewById(R.id.nameInput);
        profileImageView = findViewById(R.id.UploadImagee); // ShapeableImageView to display profile image

        // Retrieve the dropdown state passed from another activity
        isDropdownUp = getIntent().getBooleanExtra("dropdown_state", false);

        // Fetch user data from Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

            // Attach a ValueEventListener to listen for real-time updates
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue(String.class);
                        String profileImageUrl = snapshot.child("profileImage").getValue(String.class);

                        // Update UI with new data
                        nameInput.setText(username != null ? username : "Username");
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(Profile_Settings.this)
                                    .load(profileImageUrl)
                                    .placeholder(R.drawable.image_upload)
                                    .error(R.drawable.image_upload)
                                    .centerCrop()
                                    .into(profileImageView);
                        } else {
                            profileImageView.setImageResource(R.drawable.image_upload); // Default image
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Profile_Settings.this, "Failed to load profile data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Logout button
        ImageView logoutBtn = findViewById(R.id.logout);  // Your logout ImageView
        logoutBtn.setOnClickListener(v -> {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();

            // Show a toast message for successful logout
            Toast.makeText(Profile_Settings.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Redirect to the Logo_Page_Activity2
            Intent intent = new Intent(Profile_Settings.this, Logo_Page_Activity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // Finish Profile_Settings activity to prevent going back to it
            finish();
        });



        // Linked the "to pay" button to purchase
        Button pay_btn = findViewById(R.id.to_pay);
        pay_btn.setOnClickListener(v -> {
            startActivity(new Intent(Profile_Settings.this, Profile_User_Pay.class));
        });

        // Linked the "to ship" button to purchase
        Button ship_btn = findViewById(R.id.to_ship);
        ship_btn.setOnClickListener(v -> {
            startActivity(new Intent(Profile_Settings.this, Profile_User_Ship.class));
        });

        // Linked the "to receive" button to purchase
        Button receive_btn = findViewById(R.id.to_receive);
        receive_btn.setOnClickListener(v -> {
            startActivity(new Intent(Profile_Settings.this, Profile_User_To_Receive.class));
        });

    }

    // Helper method to generate a default username
    private String generateDefaultUsername() {
        String consonants = "BCDFGHJKLMNPQRSTVWXYZ"; // String containing consonants
        StringBuilder usernameBuilder = new StringBuilder();
        Random random = new Random();

        // Generate 6 random consonants
        for (int i = 0; i < 6; i++) {
            char randomConsonant = consonants.charAt(random.nextInt(consonants.length()));
            usernameBuilder.append(randomConsonant);
        }

        // Add a single random digit (0-9)
        int randomDigit = random.nextInt(10);
        usernameBuilder.append(randomDigit);

        return usernameBuilder.toString();
    }

    // Handle the "up" arrow or back button
    public void settings_drop_up(View view) {
        Intent intent = new Intent();
        intent.putExtra("dropdown_state", !isDropdownUp);  // Toggle the state
        setResult(RESULT_OK, intent);
        finish(); // Finish this activity to return to Profile_User
    }

    @Override
    public void onBackPressed() {
        // Handle the back press similarly
        Intent intent = new Intent();
        intent.putExtra("dropdown_state", !isDropdownUp);  // Toggle the state
        setResult(RESULT_OK, intent);
        super.onBackPressed(); // Finish this activity and return to the previous screen
    }

    // Start other activities
    public void profile_settings(View view) {
        startActivity(new Intent(Profile_Settings.this, Profile_Settings_information.class));
    }

    public void address_settings(View view) {
        startActivity(new Intent(Profile_Settings.this, Profile_Settings_Address.class));
    }

    public void change_password(View view) {
        Intent intent = new Intent(Profile_Settings.this, Profile_Settings_Change_Password.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void MFA_btn(View view) {
        Log.d("Profile_Settings", "Navigating to Profile_Settings_MFA");
        Intent intent = new Intent(Profile_Settings.this, Profile_Settings_MFA.class);
        startActivity(intent);
    }




    public void log_out(View view){
        startActivity(new Intent(Profile_Settings.this, sign_in.class));
    }
}







