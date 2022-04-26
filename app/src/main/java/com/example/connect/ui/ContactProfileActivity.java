package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.listeners.UsersListeners;
import com.example.connect.users.model.UserModel;
import com.example.connect.users.network.UsersApiClient;
import com.example.connect.users.model.UserResponse;
import com.facebook.common.file.FileUtils;
import com.facebook.common.util.UriUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.util.FileUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Objects;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactProfileActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    private TextView phoneNoTextView;
    private CircleImageView profileImage;
    private ImageView mBackButton, audioCall, videoCall, chat;
    private FloatingActionButton mFloatingActionButton;
    private Button mEditContact;

    private UsersListeners mUsersListeners;

    String id, fullName, phoneNo, image, gender;
    String selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        mBackButton = findViewById(R.id.contacts_back_btn);
        fullNameTextView = findViewById(R.id.contact_name_text_view);
        phoneNoTextView = findViewById(R.id.contact_phone_text_view);
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
        image = getIntent().getStringExtra("image");

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
                        .compress(620)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(620, 620)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = Objects.requireNonNull(data).getData();
        selectedImage = UriUtil.getRealPathFromUri(getContentResolver(), uri);
        profileImage.setImageURI(uri);

        uploadFileToServer();
    }

    private void uploadFileToServer() {
        mEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(selectedImage);
                // create RequestBody instance from file
                RequestBody requestFile =
                        RequestBody.create(file, MediaType.parse("image/*"));

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                Call<UserModel> uploadFileCall = UsersApiClient.getService().uploadUserImage(phoneNo,
                        body, UsersApiClient.API_KEY);
                uploadFileCall.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        Toast.makeText(ContactProfileActivity.this, "Successful upload", Toast.LENGTH_SHORT).show();
                        if (response.isSuccessful()) {
                            Log.d("uploadImage", "Response: " + response.body());
                        } else {
                            Log.d("uploadImage", "Response code: " + response.code() + ", Response:" + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Log.d("uploadImage", "Response: " + t.getLocalizedMessage());
                        Toast.makeText(ContactProfileActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

        private void initiateAudioMeeting() {
        audioCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mUsersListeners.initiateAudioMeeting();
                Intent intent = new Intent(ContactProfileActivity.this, OutGoingCallActivity.class);
                intent.putExtra("fullName", fullName);
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