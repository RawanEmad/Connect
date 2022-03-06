package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.connect.R;

public class OutGoingCallActivity extends AppCompatActivity {

    private ImageView mBackButton;
    private TextView fullNameTextView;
    private TextView prefixTextView;

    String fullName, prefix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_going_call);

        mBackButton = findViewById(R.id.contacts_back_btn);
        fullNameTextView = findViewById(R.id.contact_name_text_view);
        prefixTextView = findViewById(R.id.contact_prefix);

        //Get all the data from Intent
        fullName = getIntent().getStringExtra("fullName");
        prefix = getIntent().getStringExtra("prefix");

        displayUserData();
        callPreviousScreen();
    }

    private void displayUserData() {
        fullNameTextView.setText(fullName);
        prefixTextView.setText(prefix);
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