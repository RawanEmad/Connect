package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.adapters.ChatAdapter;
import com.example.connect.models.ChatMessages;
import com.example.connect.utilities.Constants;
import com.example.connect.utilities.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    private ImageView mBackButton;
    private RecyclerView mRecyclerView;
    private FrameLayout sendMessageBtn;
    private ProgressBar mProgressBar;
    private EditText inputMessage;

    private ArrayList<ChatMessages> chatMessages;
    private ChatAdapter chatAdapter;
    private SessionManager sessionManager;
    private FirebaseFirestore database;

    String senderId, senderName, senderImage, receiverId, fullName, phoneNo, image, conversationId, previousActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fullNameTextView = findViewById(R.id.receiver_name_text);
        mRecyclerView = findViewById(R.id.chat_recycler_view);
        inputMessage = findViewById(R.id.input_message);
        sendMessageBtn = findViewById(R.id.layout_send);
        mProgressBar = findViewById(R.id.chat_progress_bar);
        mBackButton = findViewById(R.id.chat_back_btn);

        //Get data from Intent
        previousActivity = getIntent().getStringExtra("activity");

        //Get all the data
        receiverId = Constants.KEY_ID;
        fullName = Constants.KEY_FULL_NAME;
        phoneNo = Constants.KEY_PHONE_NO;
        image = Constants.KEY_IMAGE;

        fullNameTextView.setText(fullName);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        initChat();
        setListeners();
        listenMessages();
    }

    private void initChat() {
        sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUserDetailsFromSession();
        senderId = usersDetails.get(SessionManager.KEY_ID);
        senderName = usersDetails.get(SessionManager.KEY_FULLNAME);
        senderImage = usersDetails.get(SessionManager.KEY_IMAGE);
        chatMessages = new ArrayList<ChatMessages>();
        chatAdapter = new ChatAdapter(chatMessages, image, senderId);
        mRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, senderId);
        message.put(Constants.KEY_RECEIVER_ID, receiverId);
        message.put(Constants.KEY_MESSAGE, inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message)
            .addOnSuccessListener(documentReference -> {
                Toast.makeText(ChatActivity.this, "Message sent ", Toast.LENGTH_SHORT).show();
            });
        if (conversationId != null) {
            updateConversation(inputMessage.getText().toString());
        } else {
            HashMap<String, Object> conversation = new HashMap<>();
            conversation.put(Constants.KEY_SENDER_ID, senderId);
            conversation.put(Constants.KEY_SENDER_NAME, senderName);
            conversation.put(Constants.KEY_SENDER_IMAGE, senderImage);
            conversation.put(Constants.KEY_RECEIVER_ID, receiverId);
            conversation.put(Constants.KEY_RECEIVER_NAME, fullName);
            conversation.put(Constants.KEY_RECEIVER_IMAGE, image);
            conversation.put(Constants.KEY_LAST_MESSAGE, inputMessage.getText().toString());
            conversation.put(Constants.KEY_TIMESTAMP, new Date());
            addConversation(conversation);
        }
        inputMessage.setText(null);
    }

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a",
                Locale.getDefault()).format(date);
    }

    private void listenMessages() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, senderId)
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            Log.d("Firestore chat error", error.getMessage());
        }
        if (value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessages chatMessage = new ChatMessages();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                    //Log.d("Firestore chat", chatMessage.message);
                }
                Log.d("Firestore check", String.valueOf(chatAdapter.getItemCount()));
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                mRecyclerView.smoothScrollToPosition(chatMessages.size()-1);
            }
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mProgressBar.setVisibility(View.GONE);
        if (conversationId == null) {
            checkForConversation();
        }
    };

    private void addConversation(HashMap<String, Object> conversation) {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversation)
                .addOnSuccessListener(documentReference -> conversationId = documentReference.getId());
    }

    private void updateConversation(String message) {
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversationId);
        documentReference.update(
                Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date()
        );
    }

    private void checkForConversation() {
        if (chatMessages.size() != 0) {
            checkForConversationRemotely(senderId, receiverId);
            checkForConversationRemotely(receiverId, senderId);
        }
    }

    private void checkForConversationRemotely(String senderId, String receiverId) {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversationsOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversationsOnCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversationId = documentSnapshot.getId();
        }
    };

    private void setListeners() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, ContactProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }
}