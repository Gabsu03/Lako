package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Forgot_Password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

    }

    public void Back_Login(View view) {
        startActivity(new Intent(Forgot_Password.this, sign_in.class));
    }

    public void reset_email(View view) {
        startActivity(new Intent(Forgot_Password.this, Forgot_Password_Email.class));
    }
    public void security_questions(View view) {
        startActivity(new Intent(Forgot_Password.this, Security_Questions_reset_pass.class));
    }
}