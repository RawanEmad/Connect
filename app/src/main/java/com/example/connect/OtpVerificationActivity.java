package com.example.connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaos.view.PinView;

public class OtpVerificationActivity extends AppCompatActivity {

    //variables
    private ImageView mCloseButton;
    private Button mVerifyButton;

    //data variables
    private PinView mPinView;
    private String codeBySystem;
    String fullName, phoneNo, password, confirmPassword, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        //declare variables
        mCloseButton = findViewById(R.id.otp_close_btn);
        mVerifyButton = findViewById(R.id.verify_btn);
        TextView userPhoneText = findViewById(R.id.phone_no_text_view);

        //declare data variables
        mPinView = findViewById(R.id.pin_view);

        //Get all the data from Intent
        fullName = getIntent().getStringExtra("fullName");
        phoneNo = getIntent().getStringExtra("phoneNo");
        password = getIntent().getStringExtra("password");
        confirmPassword = getIntent().getStringExtra("confirmPassword");
        gender = getIntent().getStringExtra("gender");

        userPhoneText.setText(phoneNo);

        callRegisterScreen();
        callMainScreen();
    }

    private void callRegisterScreen() {
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtpVerificationActivity.this, StartUpScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callMainScreen() {
        mVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtpVerificationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}