package com.example.connect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connect.R;

public class UpdatePasswordSuccessMessage extends AppCompatActivity {

    //variables
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass_success);

        //declare variables
        mLoginButton = findViewById(R.id.pass_updated_login_btn);

        callLoginScreen();
    }

    private void callLoginScreen() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdatePasswordSuccessMessage.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}