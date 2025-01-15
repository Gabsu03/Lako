package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lako.util.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class User_View_Loading_Screen extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ArrayList<CartItem> checkoutItems;
    private FirebaseUser user;
    private TextView datePlacedOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_view_loading_screen);

        datePlacedOrder = findViewById(R.id.date_placed_order);
        String currentDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date());
        datePlacedOrder.setText("Date Placed: " + currentDate);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        checkoutItems = getIntent().getParcelableArrayListExtra("checkoutItems");
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && checkoutItems != null) {
            processOrder();
        }
    }

    private void processOrder() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid());

        Map<String, Object> orderData = new HashMap<>();
        orderData.put("userId", user.getUid());
        orderData.put("items", checkoutItems);
        orderData.put("status", "Pending");
        orderData.put("datePlaced", new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date()));

        databaseReference.push().setValue(orderData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(User_View_Loading_Screen.this, Art_Home.class);
                    startActivity(intent);
                    finish();
                }, 2000);
            }
        });
    }
}
