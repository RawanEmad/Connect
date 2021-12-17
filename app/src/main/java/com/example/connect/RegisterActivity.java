package com.example.connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    String[] genderListItems = {"Male","Female"};
    ArrayAdapter<String> mArrayAdapter;

    //variables
    private ImageView mBackButton;
    private Button mRegisterButton;
    private TextView mLoginButton;
    private AutoCompleteTextView mGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //declare variables
        mBackButton = findViewById(R.id.register_back_btn);
        mRegisterButton = findViewById(R.id.register_btn);
        mLoginButton = findViewById(R.id.login_text_view);
        mGender = findViewById(R.id.input_gender);

        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.gender_list_items, genderListItems);

        mGender.setAdapter(mArrayAdapter);

        callStartUpScreen();
        callOtpVerificationScreen();
        callLoginScreen();
    }

    private void callStartUpScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, StartUpScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callOtpVerificationScreen() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, OtpVerificationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callLoginScreen() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}