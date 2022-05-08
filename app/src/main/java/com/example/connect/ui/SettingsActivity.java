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

public class SettingsActivity extends AppCompatActivity {

    private TextView mFullName, mPhoneNo;
    private CircleImageView mProfileImage;
    private ImageView mBackButton, mEditProfile, mEditPassword, mNotifications, mDeleteAccount, mLogout;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mBackButton = findViewById(R.id.profile_back_btn);
        mProfileImage = findViewById(R.id.user_profile_image);
        mFullName = findViewById(R.id.user_name_text_view);
        mPhoneNo = findViewById(R.id.user_phone_text_view);
        mEditProfile = findViewById(R.id.edit_profile_btn);
        mEditPassword = findViewById(R.id.edit_pass_btn);

        displayUserData();
        callPreviousScreen();
        callEditProfileScreen();
        callEditPasswordScreen();
    }

    private void callEditPasswordScreen() {
        mEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callEditProfileScreen() {
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callPreviousScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayUserData() {
        sessionManager = new SessionManager(SettingsActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUserDetailsFromSession();

        String fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
        String phoneNo = usersDetails.get(SessionManager.KEY_PHONENO);
        String image = usersDetails.get(SessionManager.KEY_IMAGE);

        mFullName.setText(fullName);
        mPhoneNo.setText(phoneNo);

        //Adding Glide library to display images
        Glide.with(getApplicationContext())
                .load(image)
                .into(mProfileImage);
    }
}