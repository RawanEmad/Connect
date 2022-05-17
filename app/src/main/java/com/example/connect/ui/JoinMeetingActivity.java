package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.utilities.SessionManager;
import com.google.android.material.textfield.TextInputLayout;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class JoinMeetingActivity extends AppCompatActivity {

    private ImageView mBackButton;
    private Button mJoinButton;
    private TextInputLayout mRoomCode;

    private JitsiMeetUserInfo mUserInfo;
    private SessionManager mSessionManager;
    private String meetingRoom;
    private String id, fullName, phoneNo, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_meeting);

        mBackButton = findViewById(R.id.join_meeting_back_btn);
        mRoomCode = findViewById(R.id.join_meeting_input_room_code);
        mJoinButton = findViewById(R.id.join_meeting_btn);

        mSessionManager = new SessionManager(JoinMeetingActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = mSessionManager.getUserDetailsFromSession();

        id = usersDetails.get(SessionManager.KEY_ID);
        fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
        phoneNo = usersDetails.get(SessionManager.KEY_PHONENO);
        image = usersDetails.get(SessionManager.KEY_IMAGE);

        mUserInfo = new JitsiMeetUserInfo();
        mUserInfo.setDisplayName(fullName);
        mUserInfo.setEmail(phoneNo);
        try {
            mUserInfo.setAvatar(new URL(image));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        meetingRoom = id + "_" + UUID.randomUUID().toString().substring(0,5);
        mRoomCode.getEditText().setText(meetingRoom);

        initiateMeeting();
        callPreviousScreen();
    }

    private void callPreviousScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinMeetingActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initiateMeeting() {
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String room_code = Objects.requireNonNull(mRoomCode.getEditText()).getText().toString().trim();

                if (room_code.isEmpty()) {
                    Toast.makeText(JoinMeetingActivity.this, "Room code can't be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                .setServerURL(new URL("https://meet.jit.si"))
                                .setUserInfo(mUserInfo)
                                .setRoom(room_code)
                                .setAudioOnly(true)
                                .build();

                        JitsiMeetActivity.launch(JoinMeetingActivity.this, options);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}