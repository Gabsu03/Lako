package com.example.lako;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.adapters.MessageAdapter;
import com.example.lako.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private MessageAdapter adapter;
    private List<Message> messages;
    private DatabaseReference messagesRef;

    private String chatId;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recycler_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messages = new ArrayList<>();
        adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);

        chatId = getIntent().getStringExtra("chatId");
        if (TextUtils.isEmpty(chatId)) {
            Log.e(TAG, "Chat ID is null or empty");
            finish();
            return;
        }

        currentUserId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (currentUserId == null) {
            Log.e(TAG, "Current user is not authenticated");
            finish();
            return;
        }

        messagesRef = FirebaseDatabase.getInstance().getReference("Messages").child(chatId);

        loadMessages();

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        messagesRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    if (message != null) {
                        messages.add(message);
                    }
                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to load messages: " + error.getMessage());
            }
        });
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            String messageId = messagesRef.push().getKey();
            if (messageId != null) {
                long timestamp = System.currentTimeMillis();
                Message message = new Message(text, timestamp, currentUserId);

                messagesRef.child(messageId).setValue(message).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        messageInput.setText("");
                        recyclerView.scrollToPosition(messages.size() - 1);
                    } else {
                        Log.e(TAG, "Failed to send message: " + task.getException());
                    }
                });
            } else {
                Log.e(TAG, "Message ID is null");
            }
        }
    }
}
