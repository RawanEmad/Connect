package com.example.connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OtpVerificationActivity extends AppCompatActivity {

    //variables
    private ImageView mCloseButton;
    private Button mVerifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        //declare variables
        mCloseButton = findViewById(R.id.otp_close_btn);
        mVerifyButton = findViewById(R.id.verify_btn);

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