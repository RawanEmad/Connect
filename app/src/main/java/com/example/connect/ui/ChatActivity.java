package com.example.connect.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Build;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
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

    String senderId, receiverId, fullName, phoneNo, image, gender;

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

        //Get all the data from Intent
        receiverId = getIntent().getStringExtra("id");
        fullName = getIntent().getStringExtra("fullName");
        phoneNo = getIntent().getStringExtra("phoneNo");
        gender = getIntent().getStringExtra("gender");
        image = getIntent().getStringExtra("image");

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
                    Log.d("Firestore chat", chatMessage.message);
                }
                //mRecyclerView.setAdapter(chatAdapter);
                //chatAdapter.notifyDataSetChanged();
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
    };

    private void setListeners() {
        mBackButton.setOnClickListener(v -> onBackPressed());

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }
}