package com.example.lako;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class sign_in extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
    }

    //Back button Sign In
    public void sign_in_back(View view) {
        startActivity(new Intent(sign_in.this, Logo_Page_Activity2.class));
    }

    public void Register(View view){
        startActivity(new Intent(sign_in.this, sign_up.class));
    }

}