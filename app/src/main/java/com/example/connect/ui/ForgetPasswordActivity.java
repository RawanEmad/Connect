package com.example.connect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connect.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    //variables
    private ImageView mArrowBack;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //declare variables
        mArrowBack = findViewById(R.id.back_forget_pass);
        mNextButton = findViewById(R.id.forget_pass_next_btn);

        callLoginScreen();
        callForgetPasswordNextScreen();
    }

    private void callForgetPasswordNextScreen() {
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPasswordActivity.this, SetNewPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callLoginScreen() {
        mArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}