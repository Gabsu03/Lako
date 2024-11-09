package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Forgot_Password_Email extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_email);

    }

    public void Back_Login_email(View view) {
        startActivity(new Intent(Forgot_Password_Email.this, sign_in.class));
    }
    public void security_question(View view) {
        startActivity(new Intent(Forgot_Password_Email.this, Security_Questions_reset_pass.class));
    }
}