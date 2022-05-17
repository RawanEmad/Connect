package com.example.connect.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.connect.R;
import com.example.connect.models.ChatMessages;
import com.example.connect.ui.ChatActivity;
import com.example.connect.utilities.Constants;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversationViewHolder>{

    private Context mContext;

    private final ArrayList<ChatMessages> chatMessages;

    public RecentConversationsAdapter(ArrayList<ChatMessages> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_container_recent_conversations, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        ChatMessages chatMessage = chatMessages.get(position);

        holder.getTextName().setText(chatMessage.conversationName);
        holder.getTextMessage().setText(chatMessage.message);
        //Adding Glide library to display images
        Glide.with(mContext)
                .load(chatMessage.conversationImage)
                .into(holder.profileImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.KEY_ID = chatMessage.conversationId;
                Constants.KEY_FULL_NAME = chatMessage.conversationName;
                Constants.KEY_IMAGE = chatMessage.conversationImage;

                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("activity", "recentConversations");
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textMessage;
        CircleImageView profileImage;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.recent_chat_name);
            textMessage = itemView.findViewById(R.id.recent_message_text);
            profileImage = itemView.findViewById(R.id.recent_chat_image);
        }

        public TextView getTextName() {
            return textName;
        }

        public TextView getTextMessage() {
            return textMessage;
        }
    }
}
