package com.example.lako.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.R;
import com.example.lako.models.ChatRoom;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {

    private List<ChatRoom> chatRooms;
    private OnChatRoomClickListener listener;

    public interface OnChatRoomClickListener {
        void onChatRoomClick(ChatRoom chatRoom);
    }

    public ChatRoomAdapter(List<ChatRoom> chatRooms, OnChatRoomClickListener listener) {
        this.chatRooms = chatRooms;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room, parent, false);
        return new ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        ChatRoom chatRoom = chatRooms.get(position);
        holder.chatRoomName.setText(chatRoom.getShopName());
        holder.lastMessage.setText(chatRoom.getLastMessage());

        holder.itemView.setOnClickListener(v -> listener.onChatRoomClick(chatRoom));
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    public static class ChatRoomViewHolder extends RecyclerView.ViewHolder {

        TextView chatRoomName, lastMessage;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            chatRoomName = itemView.findViewById(R.id.chat_room_name);
            lastMessage = itemView.findViewById(R.id.last_message);
        }
    }
}
