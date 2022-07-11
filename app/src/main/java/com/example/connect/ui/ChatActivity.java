package com.example.connect.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.example.connect.R;
import com.example.connect.adapters.ChatAdapter;
import com.example.connect.firebase.network.FirebaseMessagingClient;
import com.example.connect.firebase.network.NotificationResponse;
import com.example.connect.models.ChatMessages;
import com.example.connect.users.network.UsersApiClient;
import com.example.connect.users.response.UserResponse;
import com.example.connect.utilities.Constants;
import com.example.connect.utilities.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    private ImageView mBackButton;
    private RecyclerView mRecyclerView;
    private AppCompatImageView sendMessageBtn;
    private ProgressBar mProgressBar;
    private EditText inputMessage;

    private RecordButton recordButton;
    private RecordView recordView;
    private MediaRecorder mediaRecorder = null;
    private String audioPath = null;

    private ArrayList<ChatMessages> chatMessages;
    private ChatAdapter chatAdapter;
    private SessionManager sessionManager;
    private FirebaseFirestore database;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_REQUEST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    String senderId, senderName, senderImage, receiverId, senderToken, receiverToken,
            messageType, fullName, phoneNo, image, conversationId, previousActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fullNameTextView = findViewById(R.id.receiver_name_text);
        mRecyclerView = findViewById(R.id.chat_recycler_view);
        inputMessage = findViewById(R.id.input_message);
        sendMessageBtn = findViewById(R.id.send_text_btn);
        mProgressBar = findViewById(R.id.chat_progress_bar);
        mBackButton = findViewById(R.id.chat_back_btn);
        recordButton = findViewById(R.id.record_btn);
        recordView = findViewById(R.id.record_view);

        //Get data from Intent
        previousActivity = getIntent().getStringExtra("activity");

        //Get all the data
        receiverId = Constants.KEY_ID;
        fullName = Constants.KEY_FULL_NAME;
        phoneNo = Constants.KEY_PHONE_NO;
        image = Constants.KEY_IMAGE;
        receiverToken = Constants.KEY_FCM_TOKEN;
        messageType = Constants.KEY_TYPE;

        fullNameTextView.setText(fullName);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    sendMessageBtn.setVisibility(View.GONE);
                    recordButton.setVisibility(View.VISIBLE);

                } else {
                    recordButton.setVisibility(View.GONE);
                    sendMessageBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        checkPreviousActivity();
        initChat();
        initRecording();
        setListeners();
        listenMessages();
    }

    private void checkPreviousActivity() {
        if (previousActivity.equals("recentConversations")) {
            mBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ChatActivity.this, RecentConversationsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else if (previousActivity.equals("contactProfile")) {
            mBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ChatActivity.this, ContactProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void sendNotification(JsonObject messageBody) {
        Call<NotificationResponse> notificationResponseCall = FirebaseMessagingClient.getService().sendRemoteMessage(
                Constants.getRemoteMsgHeaders(),
                messageBody);
        notificationResponseCall.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            JSONObject responseJSON = new JSONObject(response.body().toString());
                            JSONArray results = responseJSON.getJSONArray("results");
                            if (responseJSON.getInt("failure") == 1) {
                                JSONObject error = (JSONObject) results.get(0);
                                showToast(error.getString("error"));
                                return;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showToast("Notification sent successfully");
                } else {
                    showToast("Error :" + response.code() + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                showToast(t.getLocalizedMessage());
            }
        });
    }

    private void initChat() {
        sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUserDetailsFromSession();
        senderId = usersDetails.get(SessionManager.KEY_ID);
        senderName = usersDetails.get(SessionManager.KEY_FULLNAME);
        senderImage = usersDetails.get(SessionManager.KEY_IMAGE);
        senderToken = usersDetails.get(SessionManager.KEY_FCM_TOKEN);
        chatMessages = new ArrayList<ChatMessages>();
        chatAdapter = new ChatAdapter(chatMessages, image, senderId, messageType);
        mRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, senderId);
        message.put(Constants.KEY_RECEIVER_ID, receiverId);
        message.put(Constants.KEY_MESSAGE, inputMessage.getText().toString());
        message.put(Constants.KEY_TYPE, "text");
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message)
            .addOnSuccessListener(documentReference -> {
                showToast("Message sent ");
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

        if (!receiverToken.equals("no") && !receiverToken.equals("fcmToken")) {
            try {
                JsonObject data = new JsonObject();
                data.addProperty("userId", senderId);
                data.addProperty("name", senderName);
                data.addProperty("fcmToken", senderToken);
                data.addProperty("message", inputMessage.getText().toString());

                JsonObject body = new JsonObject();
                body.add(Constants.REMOTE_MSG_DATA, data);
                body.addProperty(Constants.REMOTE_MSG_REGISTRATION_IDS, receiverToken);
                Log.d("fcmToken", "receiverToken: " + receiverToken + "\nbody: " + body);

                sendNotification(body);
            } catch (Exception exception) {
                showToast(exception.getMessage());
            }
        } else
            Log.d("fcmToken", "receiverToken: " + receiverToken);

        inputMessage.setText(null);
    }

    public static void verifyPermissions(Activity activity) {
        // Check if we have write permission
        int recordPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (recordPermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED
            || writePermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_REQUEST,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void initRecording() {
        recordButton.setRecordView(recordView);
        recordButton.setListenForRecord(false);

        recordButton.setOnClickListener(View -> {
            verifyPermissions(ChatActivity.this);
            recordButton.setListenForRecord(true);
//            if (permissions.isRecordingOk(ChatActivity.this)) {
//                recordButton.setListenForRecord(true);
//            } else {
//                permissions.requestRecording(ChatActivity.this);
//            }
        });

        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..
                Log.d("RecordView", "onStart");

                setUpRecording();

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                inputMessage.setVisibility(View.GONE);
                recordView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                //On Swipe To Cancel
                Log.d("RecordView", "onCancel");

                try {
                    mediaRecorder.reset();
                    mediaRecorder.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                File file = new File(audioPath);
                if (file.exists())
                    file.delete();

                recordView.setVisibility(View.GONE);
                inputMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish(long recordTime) {
                //Stop Recording..
                Log.d("RecordView", "onFinish");

                try {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                recordView.setVisibility(View.GONE);
                inputMessage.setVisibility(View.VISIBLE);

                sendRecodingMessage(audioPath);
            }

            @Override
            public void onLessThanSecond() {
                //When the record time is less than One Second
                Log.d("RecordView", "onLessThanSecond");

                try {
                    mediaRecorder.reset();
                    mediaRecorder.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                File file = new File(audioPath);
                if (file.exists())
                    file.delete();

                recordView.setVisibility(View.GONE);
                inputMessage.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setUpRecording() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music");

        if (!file.exists())
            file.mkdirs();
        audioPath = file.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".mp3";

        mediaRecorder.setOutputFile(audioPath);
    }

    private void sendRecodingMessage(String audioPath) {
        if (conversationId == null)
            showToast("Send simple message first");
        else {
            StorageReference reference = storageReference.child(conversationId + "/Media/Recording/" + System.currentTimeMillis());
            Uri audioFile = Uri.fromFile(new File(audioPath));

            reference.putFile(audioFile).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("FirebaseStorage", e.getMessage());
                }
            });
            reference.putFile(audioFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> audioUrl = taskSnapshot.getStorage().getDownloadUrl();

                    audioUrl.addOnCompleteListener(path -> {
                        if (path.isSuccessful()) {
                            Log.d("FirebaseStorage", "path successful");
                            String url = path.getResult().toString();
                            if (conversationId == null) {
                                initChat();
                            } else {
                                HashMap<String, Object> voiceMessage = new HashMap<>();
                                voiceMessage.put(Constants.KEY_SENDER_ID, senderId);
                                voiceMessage.put(Constants.KEY_RECEIVER_ID, receiverId);
                                voiceMessage.put(Constants.KEY_MESSAGE, url);
                                voiceMessage.put(Constants.KEY_TYPE, "recording");
                                voiceMessage.put(Constants.KEY_TIMESTAMP, new Date());
                                databaseReference.push().setValue(voiceMessage);
                                Log.d("FirebaseStorage", voiceMessage.toString());
                                database.collection(Constants.KEY_COLLECTION_CHAT).add(voiceMessage)
                                        .addOnSuccessListener(documentReference -> {
                                            showToast("Voice Message sent ");
                                        });
                            }
                        }
                    });
                }
            });
        }
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
                    chatMessage.type = documentChange.getDocument().getString(Constants.KEY_TYPE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                    Log.d("Firestore chat", chatMessage.message);
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
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }
}