package com.example.lako;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_in extends AppCompatActivity {

    // AUTHENTICATION
    TextInputEditText editTextemail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth eAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);



        //Int
        editTextemail = findViewById(R.id.email_signin);
        editTextPassword = findViewById(R.id.password_signin);
        buttonLogin = findViewById(R.id.Sign_in_button);
        eAuth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextemail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(sign_in.this, "Email is Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(sign_in.this, "Password is Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    Toast.makeText(sign_in.this, "Password is too weak", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Pattern for password strength
                String passwordPattern = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).+";
                Pattern pattern = Pattern.compile(passwordPattern);
                Matcher matcher = pattern.matcher(password);

                if (!matcher.matches()) {
                    Toast.makeText(sign_in.this, "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 numeric digit, and 1 special character", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Authenticate user login
                eAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Check if email is verified
                            FirebaseUser user = eAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // If email is verified, proceed to the main activity
                                Toast.makeText(sign_in.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                // If email is not verified, sign the user out and show a verification message
                                eAuth.signOut(); // Sign out the user
                                Toast.makeText(sign_in.this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // If authentication fails, show error message
                            Toast.makeText(sign_in.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    // Back button Sign In
    public void sign_in_back(View view) {
        startActivity(new Intent(sign_in.this, Logo_Page_Activity2.class));
    }

    public void Register(View view) {
        startActivity(new Intent(sign_in.this, sign_up.class));
    }

    public void Forgot_Pass(View view) {
        startActivity(new Intent(sign_in.this, Forgot_Password.class));
    }

}
