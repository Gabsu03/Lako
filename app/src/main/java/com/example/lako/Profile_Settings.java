package com.example.lako;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_Settings extends AppCompatActivity {

    private boolean isDropdownUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        // Retrieve the dropdown state passed from another activity
        isDropdownUp = getIntent().getBooleanExtra("dropdown_state", false);

        // Find the ImageView for starting Profile_Settings_Purchase activity
        ImageView imageView4 = findViewById(R.id.imageView4);

        // Set an OnClickListener to the ImageView
        imageView4.setOnClickListener(v -> {
            // Start the Profile_Settings_Purchase activity when clicked
            startActivity(new Intent(Profile_Settings.this, Profile_Settings_Purchase.class));
        });

        // Linked the "to pay" button to purchase
        Button pay_btn = findViewById(R.id.to_pay);
        pay_btn.setOnClickListener(v -> {
            // Start the Profile_User_Pay activity when clicked
            startActivity(new Intent(Profile_Settings.this, Profile_User_Pay.class));
        });

        // Linked the "to ship" button to purchase
        Button ship_btn = findViewById(R.id.to_ship);
        ship_btn.setOnClickListener(v -> {
            // Start the Profile_User_Ship activity when clicked
            startActivity(new Intent(Profile_Settings.this, Profile_User_Ship.class));
        });

        // Linked the "to receive" button to purchase
        Button receive_btn = findViewById(R.id.to_receive);
        receive_btn.setOnClickListener(v -> {
            // Start the Profile_User_To_Receive activity when clicked
            startActivity(new Intent(Profile_Settings.this, Profile_User_To_Receive.class));
        });

        // Linked the "to review" button to purchase
        Button reviewed_btn = findViewById(R.id.to_review);
        reviewed_btn.setOnClickListener(v -> {
            // Start the Profile_User_Received activity when clicked
            startActivity(new Intent(Profile_Settings.this, Profile_User_Received.class));
        });

        //MY SHOP LINKED
        TextView my_shop_btn = findViewById(R.id.my_shop_profile_settings);
        my_shop_btn.setOnClickListener(v -> {
            // Start the Profile_User_Received activity when clicked
            startActivity(new Intent(Profile_Settings.this, Profile_My_Shop_Start.class));
        });

    }

    // Handle the "up" arrow or back button
    public void settings_drop_up(View view) {
        // Toggle the dropdown state before returning
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

    // Start Profile_Settings_MFA
    public void MFA_btn(View view) {
        Intent intent = new Intent(Profile_Settings.this, Profile_Settings_MFA.class);
        // Use the flags to control the activity stack
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}





