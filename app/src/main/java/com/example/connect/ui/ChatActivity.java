package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.connect.R;

public class ChatActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    private ImageView mBackButton;

    String id, fullName, phoneNo, image, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fullNameTextView = findViewById(R.id.receiver_name_text);
        mBackButton = findViewById(R.id.chat_back_btn);

        //Get all the data from Intent
        id = getIntent().getStringExtra("id");
        fullName = getIntent().getStringExtra("fullName");
        phoneNo = getIntent().getStringExtra("phoneNo");
        gender = getIntent().getStringExtra("gender");
        image = getIntent().getStringExtra("image");

        fullNameTextView.setText(fullName);

        callPreviousScreen();
    }

    private void callPreviousScreen() {
        mBackButton.setOnClickListener(v -> onBackPressed());
    }
}