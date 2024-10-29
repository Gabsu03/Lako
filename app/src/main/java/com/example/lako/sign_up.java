package com.example.lako;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class sign_up extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);


        Button button = findViewById(R.id.sign_in_email);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(sign_up.this,Sign_up_email.class);
                startActivity(intent);
            }
        });
    }

    //Back button Sign Up

    public void sign_up_back(View view) {
        startActivity(new Intent(sign_up.this, Logo_Page_Activity2.class));
    }

    public void Login(View view){
        startActivity(new Intent(sign_up.this, sign_in.class));
    }

    public void terms(View view) {
        startActivity(new Intent(sign_up.this, Terms_Conditions.class));
    }

}