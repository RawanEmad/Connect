package com.example.connect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connect.R;

public class UpdatePasswordActivity extends AppCompatActivity {

    //variables
    private ImageView mArrowBack;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        //declare variables
        mArrowBack = findViewById(R.id.back_update_pass);
        mNextButton = findViewById(R.id.btn_update_pass);

        callPreviousScreen();
        callForgetPasswordNextScreen();
    }

    private void callForgetPasswordNextScreen() {
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdatePasswordActivity.this, ForgetPasswordSuccessMessage.class);
                startActivity(intent);
            }
        });
    }

    private void callPreviousScreen() {
        mArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdatePasswordActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}