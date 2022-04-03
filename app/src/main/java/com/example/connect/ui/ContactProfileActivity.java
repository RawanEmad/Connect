package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.listeners.UsersListeners;
import com.example.connect.users.network.UsersApiClient;
import com.example.connect.users.model.UserResponse;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactProfileActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    private TextView phoneNoTextView;
    private TextView prefixTextView;
    private CircleImageView profileImage;
    private ImageView mBackButton, audioCall, videoCall, chat;
    private FloatingActionButton mFloatingActionButton;
    private Button mEditContact;

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
        profileImage = findViewById(R.id.contact_profile_image);
        mFloatingActionButton = findViewById(R.id.fab_edit);
        audioCall = findViewById(R.id.audio_call_btn);
        videoCall = findViewById(R.id.video_call_btn);
        chat = findViewById(R.id.chat_btn);
        mEditContact = findViewById(R.id.edit_contact_btn);

        //Get all the data from Intent
        id = getIntent().getStringExtra("id");
        fullName = getIntent().getStringExtra("fullName");
        phoneNo = getIntent().getStringExtra("phoneNo");
        gender = getIntent().getStringExtra("gender");
        prefix = getIntent().getStringExtra("prefix");

        //getUser();
        displayUserData();
        callPreviousScreen();
        editProfileImage();
        initiateAudioMeeting();
        initiateVideoMeeting();
    }

    private void displayUserData() {
        fullNameTextView.setText(fullName);
        phoneNoTextView.setText(phoneNo);
        prefixTextView.setText(prefix);
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

    private void editProfileImage() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(ContactProfileActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = Objects.requireNonNull(data).getData();
        profileImage.setImageURI(uri);
        prefixTextView.setText("");
    }

    private void initiateAudioMeeting() {
        audioCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mUsersListeners.initiateAudioMeeting();
                Intent intent = new Intent(ContactProfileActivity.this, OutGoingCallActivity.class);
                intent.putExtra("fullName", fullName);
                intent.putExtra("prefix", prefix);
                startActivity(intent);
            }
        });
    }

    private void initiateVideoMeeting() {
        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactProfileActivity.this, OutGoingCallActivity.class);
                intent.putExtra("fullName", fullName);
                intent.putExtra("prefix", prefix);
                startActivity(intent);
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