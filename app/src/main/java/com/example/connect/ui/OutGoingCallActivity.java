package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.connect.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class OutGoingCallActivity extends AppCompatActivity {

    private ImageView mBackButton;
    private TextView fullNameTextView;
    private CircleImageView profileImageView;

    String fullName, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_going_call);

        mBackButton = findViewById(R.id.contacts_back_btn);
        fullNameTextView = findViewById(R.id.contact_name_text_view);
        profileImageView = findViewById(R.id.contact_profile_image);

        //Get all the data from Intent
        fullName = getIntent().getStringExtra("fullName");
        image = getIntent().getStringExtra("image");

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
                Intent intent = new Intent(OutGoingCallActivity.this, ContactProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}