package com.example.lako;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
            return;
        }

        setContentView(R.layout.activity_main);

        // Setup the BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        // Check the intent for navigation
        String navigateTo = getIntent().getStringExtra("navigate_to");
        Fragment selectedFragment;

        if ("HomeFragment".equals(navigateTo)) {
            // Load HomeFragment when specified
            selectedFragment = new Home();
            bottomNav.setSelectedItemId(R.id.nav_Home);
        } else {
            // Default to HomeFragment
            selectedFragment = new Home();
            bottomNav.setSelectedItemId(R.id.nav_Home);
        }

        // Load the selected fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();
    }

    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.nav_Wishlist) {
            selectedFragment = new WishList();
        } else if (item.getItemId() == R.id.nav_Notification) {
            selectedFragment = new Notifications();
        } else if (item.getItemId() == R.id.nav_Home) {
            selectedFragment = new Home();
        } else if (item.getItemId() == R.id.nav_Message) {
            selectedFragment = new Message();
        } else if (item.getItemId() == R.id.nav_Profile) {
            selectedFragment = new Profile_User();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }
        return true;
    };
}
