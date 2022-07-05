package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.connect.R;
import com.example.connect.users.network.UsersApiClient;
import com.example.connect.users.response.UserResponse;
import com.example.connect.utilities.Constants;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactProfileActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    private TextView phoneNoTextView;
    private CircleImageView profileImage;
    private ImageView mBackButton, audioCall, videoCall, chat;
    private Button mEditContact;

    String id, fullName, phoneNo, image, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        mBackButton = findViewById(R.id.contacts_back_btn);
        fullNameTextView = findViewById(R.id.contact_name_text_view);
        phoneNoTextView = findViewById(R.id.contact_phone_text_view);
        profileImage = findViewById(R.id.contact_profile_image);
        audioCall = findViewById(R.id.audio_call_btn);
        videoCall = findViewById(R.id.video_call_btn);
        chat = findViewById(R.id.chat_btn);
        mEditContact = findViewById(R.id.edit_contact_btn);

        //Get all the data
        id = Constants.KEY_ID;
        fullName = Constants.KEY_FULL_NAME;
        phoneNo = Constants.KEY_PHONE_NO;
        gender = Constants.KEY_GENDER;
        image = Constants.KEY_IMAGE;

        //getUser();
        displayUserData();
        callPreviousScreen();
        initiateAudioMeeting();
        initiateVideoMeeting();
        chatUser();
    }

    private void displayUserData() {
        fullNameTextView.setText(fullName);
        phoneNoTextView.setText(phoneNo);

        //Adding Glide library to display images
        Glide.with(getApplicationContext())
                .load(image)
                .into(profileImage);
    }

    private void callPreviousScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactProfileActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void chatUser() {
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactProfileActivity.this, ChatActivity.class);
                intent.putExtra("activity", "contactProfile");
                startActivity(intent);
                finish();
            }
        });
    }

    private void initiateAudioMeeting() {
        audioCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mUsersListeners.initiateAudioMeeting();
                Intent intent = new Intent(ContactProfileActivity.this, OutGoingCallActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initiateVideoMeeting() {
        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactProfileActivity.this, OutGoingCallActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getUser() {
        Call<UserResponse> userResponseCall = UsersApiClient.getService().getUser(phoneNo, UsersApiClient.API_KEY);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    String userName = response.body().getUserData().getFullName();
                    String phoneNo = "+" + response.body().getUserData().getPhoneNo();
                    fullNameTextView.setText(userName);
                    phoneNoTextView.setText(phoneNo);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ContactProfileActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}