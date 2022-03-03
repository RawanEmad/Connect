package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.listeners.UsersListeners;
import com.example.connect.users.model.UserModel;
import com.example.connect.users.network.UsersApiClient;
import com.example.connect.users.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactProfileActivity extends AppCompatActivity {

    private ImageView mBackButton;
    private TextView fullNameTextView;
    private TextView phoneNoTextView;
    private TextView prefixTextView;
    private ImageView audioCall, videoCall, chat;

    private UsersListeners mUsersListeners;

    String id, fullName, phoneNo, prefix, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        mBackButton = findViewById(R.id.contacts_back_btn);
        fullNameTextView = findViewById(R.id.contact_name_text_view);
        phoneNoTextView = findViewById(R.id.contact_phone_text_view);
        prefixTextView = findViewById(R.id.contact_prefix);
        audioCall = findViewById(R.id.audio_call_btn);
        videoCall = findViewById(R.id.video_call_btn);
        chat = findViewById(R.id.chat_btn);

        //Get all the data from Intent
        id = getIntent().getStringExtra("id");
        fullName = getIntent().getStringExtra("fullName");
        phoneNo = getIntent().getStringExtra("phoneNo");
        gender = getIntent().getStringExtra("gender");
        prefix = getIntent().getStringExtra("prefix");

        //getUser();
        displayUserData();
        callContactsScreen();
        initiateMeeting();
    }

    private void displayUserData() {
        fullNameTextView.setText(fullName);
        phoneNoTextView.setText(phoneNo);
        prefixTextView.setText(prefix);
    }

    private void callContactsScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactProfileActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initiateAudioMeeting() {
        audioCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mUsersListeners.initiateAudioMeeting();
            }
        });
    }

    private void initiateMeeting() {
        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactProfileActivity.this, JoinMeetingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getUser() {
        Call<UserResponse> userResponseCall = UsersApiClient.getService().getUser("201066923650", UsersApiClient.API_KEY);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    String userName = response.body().getUserData().getFullName();
                    char firstLetter = userName.charAt(0);
                    String prefix = String.valueOf(firstLetter).toUpperCase();
                    String phoneNo = "+" + response.body().getUserData().getPhoneNo();
                    fullNameTextView.setText(userName);
                    phoneNoTextView.setText(phoneNo);
                    prefixTextView.setText(prefix);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ContactProfileActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}