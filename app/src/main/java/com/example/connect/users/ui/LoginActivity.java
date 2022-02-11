package com.example.connect.users.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.MainActivity;
import com.example.connect.R;
import com.example.connect.users.LoginRequest;
import com.example.connect.users.LoginResponse;
import com.example.connect.users.RegisterRequest;
import com.example.connect.users.RegisterResponse;
import com.example.connect.users.api.UsersApiClient;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //variables
    private ImageView mBackButton;
    private Button mLoginButton;
    private TextView mRegisterButton;
    private ProgressBar mProgressBar;

    //data variables
    private TextInputLayout mPhoneNo, mPassword;

    //API KEY
    private final static String API_KEY = "382395e75d624fb1478303451bc7543314ffffac6372c2aa9beb22f687e6e886b77b3ee84aeeb1a8aabad9647686d0baaa4d9a7c65ff6ef1ebc71fcde7bac14b";


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

    private void loginUser(LoginRequest loginRequest) {
        Call<LoginResponse> loginResponseCall = UsersApiClient.getService().loginUser(loginRequest, API_KEY);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Successful Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Not Completed! Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

                String _phoneNumber = Objects.requireNonNull(mPhoneNo.getEditText()).getText().toString().trim();
                String _password = Objects.requireNonNull(mPassword.getEditText()).getText().toString().trim();

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setPhoneNo(_phoneNumber);
                loginRequest.setPassword(_password);

                loginUser(loginRequest);
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