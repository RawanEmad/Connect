package com.example.connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    //variables
    private ImageView mBackButton;
    private Button mLoginButton;
    private TextView mRegisterButton;
    private ProgressBar mProgressBar;

    //data variables
    private TextInputLayout mPhoneNo, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //declare variables
        mBackButton = findViewById(R.id.login_back_btn);
        mLoginButton = findViewById(R.id.login_btn);
        mRegisterButton = findViewById(R.id.register_text_view);
        mProgressBar = findViewById(R.id.login_progress_bar);

        //declare data variables
        mPhoneNo = findViewById(R.id.login_input_phone);
        mPassword = findViewById(R.id.login_input_password);

        callStartUpScreen();
        callMainScreen();
        callRegisterScreen();
    }

    private boolean validateFields(){
        String _phoneNumber = Objects.requireNonNull(mPhoneNo.getEditText()).getText().toString().trim();
        String _password = Objects.requireNonNull(mPassword.getEditText()).getText().toString().trim();

        if (_phoneNumber.isEmpty()) {
            mPhoneNo.setError("Phone number can not be empty!");
            return false;
        } else if (_password.isEmpty()) {
            mPassword.setError("Password can not be empty!");
            return false;
        } else
            mPhoneNo.setErrorEnabled(false);
        mPassword.setErrorEnabled(false);
        return true;
    }

    private void callStartUpScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, StartUpScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callMainScreen() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validate fields
                if (!validateFields()) {
                    return;
                } //Validation succeeded and now move to next screen

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callRegisterScreen() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}