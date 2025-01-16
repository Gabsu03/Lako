package com.example.lako.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.ChatActivity;
import com.example.lako.Chat_bot;
import com.example.lako.R;
import com.example.lako.adapters.ChatRoomAdapter;
import com.example.lako.models.ChatRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Message extends Fragment {

    private RecyclerView recyclerView;
    private ChatRoomAdapter adapter;
    private List<ChatRoom> chatRooms;
    private DatabaseReference chatRef;
    private String currentUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        // Initialize chat_a_bot button
        Button chatButton = view.findViewById(R.id.chat_a_bot);
        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Chat_bot.class);
            startActivity(intent);
        });


        recyclerView = view.findViewById(R.id.messageRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chatRooms = new ArrayList<>();
        adapter = new ChatRoomAdapter(chatRooms, chatRoom -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("chatId", chatRoom.getChatId());
            intent.putExtra("username", chatRoom.getUsername());
            intent.putExtra("shopName", chatRoom.getShopName());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        chatRef = FirebaseDatabase.getInstance().getReference("Chats");
        loadCurrentUsername();
        loadChatRooms();

        return view;
    }

    private void loadCurrentUsername() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        userRef.child("username").get().addOnSuccessListener(snapshot -> {
            currentUsername = snapshot.getValue(String.class);
            // Call loadChatRooms only after username is loaded
            loadChatRooms();
        }).addOnFailureListener(e -> {
            // Handle error if username fails to load
            e.printStackTrace();
        });
    }

    private void loadChatRooms() {
        chatRef.orderByChild("username").equalTo(currentUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatRooms.clear();
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    ChatRoom chatRoom = chatSnapshot.getValue(ChatRoom.class);
                    chatRooms.add(chatRoom);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
