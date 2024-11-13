package com.example.lako;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Logo_Page_Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logo_page2);
    }

    // Sign Up
    public void Sign_up (View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Logo_Page_Activity2.this, sign_up.class));
        finish();
    }

    // Sign In
    public void Sign_In (View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Logo_Page_Activity2.this, sign_in.class));
        finish();
    }

    // About Us
    public void about_us (View view) {
        startActivity(new Intent(Logo_Page_Activity2.this, About_Us.class));
    }
}
