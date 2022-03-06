package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.connect.R;
import com.google.android.material.textfield.TextInputLayout;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class JoinMeetingActivity extends AppCompatActivity {

    private ImageView mBackButton;
    private Button mJoinButton;
    private TextInputLayout mRoomCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_meeting);

        mBackButton = findViewById(R.id.join_meeting_back_btn);
        mRoomCode = findViewById(R.id.join_meeting_input_room_code);
        mJoinButton = findViewById(R.id.join_meeting_btn);
        
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