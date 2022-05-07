package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.connect.R;
import com.example.connect.utilities.SessionManager;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class IncomingCallActivity extends AppCompatActivity {

    private ImageView mBackButton;
    private TextView fullNameTextView;
    private CircleImageView profileImageView;

    private SessionManager sessionManager;

    String id, fullName, phoneNo, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        mBackButton = findViewById(R.id.contacts_back_btn);
        fullNameTextView = findViewById(R.id.contact_name_text_view);
        profileImageView = findViewById(R.id.contact_profile_image);

        sessionManager = new SessionManager(IncomingCallActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUserDetailsFromSession();

        //Get all the data
        id = usersDetails.get(sessionManager.KEY_ID);
        fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
        phoneNo = usersDetails.get(SessionManager.KEY_PHONENO);
        image = usersDetails.get(SessionManager.KEY_IMAGE);

        displayUserData();
        callPreviousScreen();
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
                Intent intent = new Intent(IncomingCallActivity.this, ContactProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}