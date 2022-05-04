package com.example.connect.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect.R;
import com.example.connect.models.ChatMessage;
import com.example.connect.utilities.SessionManager;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;

    private final List<ChatMessage> chatMessages;
    private final Bitmap receivedProfileImage;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receivedProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receivedProfileImage = receivedProfileImage;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_container_sent_message, parent, false);
            return new ChatAdapter.SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_container_received_message, parent, false);
            return new ChatAdapter.ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receivedProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    public static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        TextView textMessage, dateTime;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.text_message);
            dateTime = itemView.findViewById(R.id.text_date_time);
        }

        public TextView getTextMessage() {
            return textMessage;
        }

        public TextView getDateTime() {
            return dateTime;
        }

        void setData(ChatMessage chatMessage) {
            getTextMessage().setText(chatMessage.message);
            getDateTime().setText(chatMessage.dateTime);
        }
    }

    public static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        TextView textMessage, dateTime;
        CircleImageView profileImage;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.text_message);
            dateTime = itemView.findViewById(R.id.text_date_time);
            profileImage = itemView.findViewById(R.id.received_profile_image);
        }

        public TextView getTextMessage() {
            return textMessage;
        }

        public TextView getDateTime() {
            return dateTime;
        }

        void setData(ChatMessage chatMessage, Bitmap receivedProfileImage) {
            getTextMessage().setText(chatMessage.message);
            getDateTime().setText(chatMessage.dateTime);
            profileImage.setImageBitmap(receivedProfileImage);
        }
    }
}
