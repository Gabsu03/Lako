package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.material.textfield.TextInputEditText;

public class sign_in extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100; // Request code for Google Sign-In
    private static final String TAG = "sign_in";

    // UI Components
    TextInputEditText editTextemail, editTextPassword;
    Button buttonLogin, buttonSignInWithEmail;

    // Firebase Auth
    FirebaseAuth eAuth;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize Firebase Auth
        eAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        editTextemail = findViewById(R.id.email_signin);
        editTextPassword = findViewById(R.id.password_signin);
        buttonLogin = findViewById(R.id.Sign_in_button);
        buttonSignInWithEmail = findViewById(R.id.Sign_in_email_button);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Replace with your client ID
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // "Sign In" button (manual email/password)
        buttonLogin.setOnClickListener(v -> handleLogin());

        // "Sign In with Email" button (Google Sign-In)
        buttonSignInWithEmail.setOnClickListener(v -> signInWithGoogle());
    }

    private void handleLogin() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return;
        }

        eAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = eAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    // Navigate to Art_Home.class on successful login
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    redirectToArtHome();
                } else {
                    eAuth.signOut();
                    Toast.makeText(this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle Google Sign-In response
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign-in failed", e);
                Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        eAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Sign-in success, navigate to Art_Home
                FirebaseUser user = eAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(this, "Google Sign-In Successful!", Toast.LENGTH_SHORT).show();
                    redirectToArtHome();
                }
            } else {
                Log.w(TAG, "Google sign-in failed", task.getException());
                Toast.makeText(this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectToArtHome() {
        // Navigate to Art_Home activity
        Intent intent = new Intent(this, Art_Home.class);
        startActivity(intent);
        finish(); // Finish the current activity to prevent going back to the login screen
    }

    public void Forgot_Pass(View view) {
        startActivity(new Intent(sign_in.this, Forgot_Password.class));
    }

    public void  Register(View view) {
        startActivity(new Intent(sign_in.this, sign_up.class));
    }

    public void  sign_in_back(View view) {
        startActivity(new Intent(sign_in.this, Logo_Page_Activity2.class));
    }
}
