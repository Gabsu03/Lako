package com.example.lako;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("profileImage").getValue(String.class); // Get profile image URL

                    // Set the user's name in the TextView or EditText (assuming you have a TextView to show the name)
                    if (firstName != null && lastName != null) {
                        nameInput.setText(firstName + " " + lastName); // Display the full name
                    } else {
                        nameInput.setText("Username7");
                    }

                    // If profile image URL exists, load it using Glide
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(Profile_Settings.this)
                                .load(profileImageUrl)  // Load the image URL from Firebase
                                .placeholder(R.drawable.image_upload)  // Placeholder if the image is loading
                                .error(R.drawable.image_upload)  // Error image if loading fails
                                .centerCrop()  // Ensure image scales properly inside the circle
                                .into(profileImageView);  // Set image into the ImageView
                    }
                }
            });
        }

        // Logout button
        ImageView logoutBtn = findViewById(R.id.logout);  // Your logout ImageView
        logoutBtn.setOnClickListener(v -> {
            // Sign out the user from Firebase
            FirebaseAuth.getInstance().signOut();

            // Show Toast message indicating logout success
            Toast.makeText(Profile_Settings.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Redirect to Sign-In activity
            Intent intent = new Intent(Profile_Settings.this, sign_in.class);
            startActivity(intent);
            finish();
        });

        // Find the ImageView for starting Profile_Settings_Purchase activity
        ImageView imageView4 = findViewById(R.id.imageView4);
        imageView4.setOnClickListener(v -> {
            startActivity(new Intent(Profile_Settings.this, Profile_Settings_Purchase.class));
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

        // Linked the "to review" button to purchase
        Button reviewed_btn = findViewById(R.id.to_review);
        reviewed_btn.setOnClickListener(v -> {
            startActivity(new Intent(Profile_Settings.this, Profile_User_Received.class));
        });

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

    public void payments_settings(View view) {
        startActivity(new Intent(Profile_Settings.this, Profile_Settings_Payment.class));
    }

    public void change_password(View view) {
        Intent intent = new Intent(Profile_Settings.this, Profile_Settings_Change_Password.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // Start Profile_Settings_MFA
    public void MFA_btn(View view) {
        Intent intent = new Intent(Profile_Settings.this, Profile_Settings_MFA.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void log_out(View view){
        startActivity(new Intent(Profile_Settings.this, sign_in.class));
    }
}







