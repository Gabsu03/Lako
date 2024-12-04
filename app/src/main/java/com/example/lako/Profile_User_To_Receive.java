package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_User_To_Receive extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_user_to_receive);
    }

    public void To_receive_purchase_back(View view) {
        // Instead of just finishing the activity, set the BottomNavigationView to Profile_User
        Intent intent = new Intent(Profile_User_To_Receive.this, MainActivity.class); // Start MainActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Clear all activities above it in the stack
        startActivity(intent);
        finish();
    }

    public void To_Pay(View view) {
        startActivity(new Intent(Profile_User_To_Receive.this, Profile_User_Pay.class));
    }

    public void All_btn(View view) {
        startActivity(new Intent(Profile_User_To_Receive.this, Profile_Settings_Purchase.class));
    }
    public void To_Ship(View view) {
        startActivity(new Intent(Profile_User_To_Receive.this, Profile_User_Ship.class));
    }

    public void Received(View view) {
        startActivity(new Intent(Profile_User_To_Receive.this, Profile_User_Received.class));
    }

}