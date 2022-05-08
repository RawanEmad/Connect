package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.utilities.SessionManager;
import com.example.connect.models.UserModel;
import com.example.connect.users.response.UserResponse;
import com.example.connect.users.request.LoginRequest;
import com.example.connect.users.network.UsersApiClient;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //variables
    private ImageView mBackButton;
    private Button mLoginButton;
    private TextView mRegisterButton, mForgetPassword;
    private CheckBox mRememberMe;
    private ProgressBar mProgressBar;

    //data variables
    private TextInputLayout mPhoneNo, mPassword;

    String _phoneNumber, _password;
    String id, fullName, phoneNo, image, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //declare variables
        mBackButton = findViewById(R.id.login_back_btn);
        mLoginButton = findViewById(R.id.login_btn);
        mRegisterButton = findViewById(R.id.register_text_view);
        mRememberMe = findViewById(R.id.remember_me_check_box);
        mForgetPassword = findViewById(R.id.forget_ur_pass);
        mProgressBar = findViewById(R.id.login_progress_bar);

        //declare data variables
        mPhoneNo = findViewById(R.id.login_input_phone);
        mPassword = findViewById(R.id.login_input_password);

        //retrieve phone number and password from shared preferences
        SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMBERME);
        if (sessionManager.checkRememberMe()) {
            HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
            mPhoneNo.getEditText().setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPHONENO));
            mPassword.getEditText().setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));
        }

        callPreviousScreen();
        callMainScreen();
        callRegisterScreen();
        callForgetPasswordScreen();
    }

    private void callForgetPasswordScreen() {
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateFields(){
        _phoneNumber = Objects.requireNonNull(mPhoneNo.getEditText()).getText().toString().trim();
        _password = Objects.requireNonNull(mPassword.getEditText()).getText().toString().trim();

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

    private void callPreviousScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, StartUpScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getUserDataForSession() {
        Call<UserResponse> userResponseCall = UsersApiClient.getService().getUser(_phoneNumber, UsersApiClient.API_KEY);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    id = response.body().getUserData().getUserId();
                    fullName = response.body().getUserData().getFullName();
                    phoneNo = response.body().getUserData().getPhoneNo();
                    image = response.body().getUserData().getProfileImage();
                    gender = response.body().getUserData().getGender();

                    //create login session
                    SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_USERSESSION);
                    sessionManager.createLoginSession(id, fullName, phoneNo, _password, image, gender);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(LoginRequest loginRequest) {
        Call<UserModel> loginResponseCall = UsersApiClient.getService().loginUser(loginRequest, UsersApiClient.API_KEY);
        loginResponseCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    //Get user data from database and create a session
                    getUserDataForSession();

                    hideDialog();

                    Toast.makeText(LoginActivity.this, "Successful Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Not Completed! Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
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

                showDialog();

                String _phoneNumber = Objects.requireNonNull(mPhoneNo.getEditText()).getText().toString().trim();
                String _password = Objects.requireNonNull(mPassword.getEditText()).getText().toString().trim();

                //create remember me session
                if (mRememberMe.isChecked()) {
                    SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMBERME);
                    sessionManager.createRememberMeSession(_phoneNumber,_password);
                }

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