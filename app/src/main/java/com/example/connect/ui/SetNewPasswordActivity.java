package com.example.connect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connect.R;

public class SetNewPasswordActivity extends AppCompatActivity {

    //variables
    private ImageView mArrowBack;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        //declare variables
        mArrowBack = findViewById(R.id.back_update_pass);
        mNextButton = findViewById(R.id.btn_update_pass);

        callMakeSelectionScreen();
        callForgetPasswordNextScreen();
    }

    private void callForgetPasswordNextScreen() {
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetNewPasswordActivity.this, ForgetPasswordSuccessMessage.class);
                startActivity(intent);
            }
        });
    }

    private void callMakeSelectionScreen() {
        mArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetNewPasswordActivity.this, OtpVerificationActivity.class);
                startActivity(intent);
            }
        });
    }
}