package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Sign_up_email extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_email);
    }

    public void sign_up_email_back(View view){
        startActivity(new Intent(Sign_up_email.this,sign_up.class));
    }

    public void Login_sign_up(View view){
        startActivity(new Intent(Sign_up_email.this,sign_in.class));
    }
}