package com.example.lako;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.lako.Fragments.Home;
import com.example.lako.Fragments.Message;
import com.example.lako.Fragments.Notifications;
import com.example.lako.Fragments.Profile_User;
import com.example.lako.Fragments.WishList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is authenticated
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // If no user is logged in, redirect to the Logo_Page_Activity2
            Intent intent = new Intent(MainActivity.this, Logo_Page_Activity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return; // Prevent further execution
        }

        setContentView(R.layout.activity_main);

        // Setup the BottomNavigationView and default fragment
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_Profile);  // Default to Profile_User when app opens
        bottomNav.setOnItemSelectedListener(navListener);

        // Check if the showProfileUser flag is passed in the intent
        if (getIntent().getBooleanExtra("showProfileUser", false)) {
            // If true, display the Profile_User fragment immediately
            Fragment selectedFragment = new Profile_User();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        } else {
            // Otherwise, load the default fragment (Profile_User)
            Fragment selectedFragment = new Profile_User();  // Profile_User as default
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
    }

    private NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        // Handle navigation between bottom navigation items
        if (item.getItemId() == R.id.nav_Wishlist) {
            selectedFragment = new WishList();
        } else if (item.getItemId() == R.id.nav_Notification) {
            selectedFragment = new Notifications();
        } else if (item.getItemId() == R.id.nav_Home) {
            selectedFragment = new Home();
        } else if (item.getItemId() == R.id.nav_Message) {
            selectedFragment = new Message();
        } else if (item.getItemId() == R.id.nav_Profile) {
            selectedFragment = new Profile_User();  // Profile_User is the fragment here
        }

        // Replace the current fragment with the selected one
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        return true;
    };
}


