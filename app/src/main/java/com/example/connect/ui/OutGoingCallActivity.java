package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.connect.R;
import com.example.connect.firebase.network.FirebaseMessagingClient;
import com.example.connect.firebase.network.FirebaseMessagingCall;
import com.example.connect.utilities.Constants;
import com.example.connect.utilities.SessionManager;
import com.google.gson.JsonObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutGoingCallActivity extends AppCompatActivity {

    private ImageView mBackButton, mCancelButton;
    private TextView fullNameTextView;
    private CircleImageView profileImageView;

    private SessionManager sessionManager;
    private String id, fullName, phoneNo, image, receiverToken, meetingType,
                senderId, senderName, senderImage, senderToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_going_call);

        mBackButton = findViewById(R.id.contacts_back_btn);
        fullNameTextView = findViewById(R.id.contact_name_text_view);
        profileImageView = findViewById(R.id.contact_profile_image);
        mCancelButton = findViewById(R.id.cancel_btn);

        //Get all the data
        id = Constants.KEY_ID;
        fullName = Constants.KEY_FULL_NAME;
        phoneNo = Constants.KEY_PHONE_NO;
        image = Constants.KEY_IMAGE;
        receiverToken = Constants.KEY_FCM_TOKEN;

        meetingType = getIntent().getStringExtra("meetingType");

        getSenderData();
        displayUserData();
        initiateMeeting(meetingType, receiverToken);
        callPreviousScreen();
    }

    private void getSenderData() {
        sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUserDetailsFromSession();
        senderId = usersDetails.get(SessionManager.KEY_ID);
        senderName = usersDetails.get(SessionManager.KEY_FULLNAME);
        senderImage = usersDetails.get(SessionManager.KEY_IMAGE);
        senderToken = usersDetails.get(SessionManager.KEY_FCM_TOKEN);
    }

    private void displayUserData() {
        fullNameTextView.setText(fullName);

        //Adding Glide library to display images
        Glide.with(getApplicationContext())
                .load(image)
                .into(profileImageView);
    }

    private void callPreviousScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OutGoingCallActivity.this, ContactProfileActivity.class);
                startActivity(intent);
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OutGoingCallActivity.this, ContactProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(OutGoingCallActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void initiateMeeting(String meetingType, String receiverToken) {
        try {
            JsonObject data = new JsonObject();
            data.addProperty(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION);
            data.addProperty(Constants.REMOTE_MSG_MEETING_TYPE, meetingType);
            data.addProperty("userId", senderId);
            data.addProperty("userName", senderName);
            data.addProperty("userImage", senderImage);
            data.addProperty(Constants.REMOTE_MSG_INVITER_TOKEN, senderToken);

            JsonObject body = new JsonObject();
            body.add(Constants.REMOTE_MSG_DATA, data);
            body.addProperty(Constants.REMOTE_MSG_REGISTRATION_IDS, receiverToken);

            sendRemoteMessage(body, Constants.REMOTE_MSG_INVITATION);
        } catch (Exception exception) {
            showToast(exception.getMessage());
            finish();
        }
    }

    private void sendRemoteMessage(JsonObject messageBody, String type) {
        Call<FirebaseMessagingCall> notificationResponseCall = FirebaseMessagingClient.getService().sendRemoteMessage(
                Constants.getRemoteMsgHeaders(),
                messageBody);
        notificationResponseCall.enqueue(new Callback<FirebaseMessagingCall>() {
            @Override
            public void onResponse(Call<FirebaseMessagingCall> call, Response<FirebaseMessagingCall> response) {
                if (response.isSuccessful()) {
                   if (type.equals(Constants.REMOTE_MSG_INVITATION)) {
                       showToast("Invitation sent successfully");
                   }
                } else {
                    showToast("Error :" + response.code() + response.errorBody());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<FirebaseMessagingCall> call, Throwable t) {
                showToast(t.getLocalizedMessage());
                finish();
            }
        });
    }

}