package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.connect.R;
import com.example.connect.firebase.network.FirebaseMessagingCall;
import com.example.connect.firebase.network.FirebaseMessagingClient;
import com.example.connect.utilities.Constants;
import com.example.connect.utilities.SessionManager;
import com.google.gson.JsonObject;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomingCallActivity extends AppCompatActivity {

    private ImageView mBackButton, mAcceptButton, mRejectButton;
    private TextView fullNameTextView, meetingTypeTextView;
    private CircleImageView profileImageView;

    private SessionManager sessionManager;

    String id, fullName, phoneNo, image, userId, userName, userImage, userToken, meetingType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        mBackButton = findViewById(R.id.contacts_back_btn);
        fullNameTextView = findViewById(R.id.contact_name_text_view);
        profileImageView = findViewById(R.id.contact_profile_image);
        meetingTypeTextView = findViewById(R.id.incoming_call_text_view);
        mAcceptButton = findViewById(R.id.confirm_btn);
        mRejectButton = findViewById(R.id.reject_btn);

        //Get data from intent
        meetingType = getIntent().getStringExtra("meetingType");
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        userImage = getIntent().getStringExtra("userImage");
        userToken = getIntent().getStringExtra("userToken");

        sessionManager = new SessionManager(IncomingCallActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUserDetailsFromSession();

        //Get all the data
        id = usersDetails.get(sessionManager.KEY_ID);
        fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
        phoneNo = usersDetails.get(SessionManager.KEY_PHONENO);
        image = usersDetails.get(SessionManager.KEY_IMAGE);

        displayUserData();
        callPreviousScreen();
        setListeners();
    }

    private void setListeners() {
        mAcceptButton.setOnClickListener(view -> sendInvitationResponse(
                Constants.REMOTE_MSG_INVITATION_ACCEPTED, userToken
        ));
        mRejectButton.setOnClickListener(view -> sendInvitationResponse(
                Constants.REMOTE_MSG_INVITATION_REJECTED, userToken
        ));
    }

    private void displayUserData() {
        fullNameTextView.setText(userName);

        //Adding Glide library to display images
        Glide.with(getApplicationContext())
                .load(userImage)
                .into(profileImageView);

        if (meetingType.equals("audio")) {
            meetingTypeTextView.setText("audio call");
        } else if (meetingType.equals("video")) {
            meetingTypeTextView.setText("video call");
        }
    }

    private void callPreviousScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IncomingCallActivity.this, ContactProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(IncomingCallActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void sendInvitationResponse(String meetingType, String receiverToken) {
        try {
            JsonObject data = new JsonObject();
            data.addProperty(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.addProperty(Constants.REMOTE_MSG_INVITATION_RESPONSE, meetingType);
            data.addProperty("userName", userName);

            JsonObject body = new JsonObject();
            body.add(Constants.REMOTE_MSG_DATA, data);
            body.addProperty(Constants.REMOTE_MSG_REGISTRATION_IDS, receiverToken);

            sendRemoteMessage(body, meetingType);
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
                    if (type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED)) {
                        JitsiMeetUserInfo mUserInfo = new JitsiMeetUserInfo();
                        mUserInfo.setDisplayName(fullName);
                        mUserInfo.setEmail(phoneNo);
                        try {
                            mUserInfo.setAvatar(new URL(image));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        try {
                            JitsiMeetConferenceOptions.Builder options = new JitsiMeetConferenceOptions.Builder();
                            options.setServerURL(new URL("https://meet.jit.si"));
                            options.setUserInfo(mUserInfo);
                            options.setRoom(getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_ROOM));
                            if (meetingType.equals("audio")) {
                                options.setVideoMuted(true);
                            }

                            JitsiMeetActivity.launch(IncomingCallActivity.this, options.build());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToast("Invitation Rejected");
                        finish();
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

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE);
            if (type != null) {
                if (type.equals(Constants.REMOTE_MSG_INVITATION_CANCELLED)) {
                    showToast("Invitation Cancelled");
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver,
                new IntentFilter(Constants.REMOTE_MSG_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitationResponseReceiver
        );
    }
}