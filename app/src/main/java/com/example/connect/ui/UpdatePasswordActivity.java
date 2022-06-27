package com.example.connect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connect.R;
import com.example.connect.users.network.UsersApiClient;
import com.example.connect.users.response.UserResponse;
import com.example.connect.utilities.SessionManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordActivity extends AppCompatActivity {

    //variables
    private ImageView mArrowBack;
    private TextView mTextView;
    private TextInputLayout mCurrentPassword, mNewPassword, mConfirmPassword;
    private Button mUpdateButton, mCancelButton;

    private SessionManager sessionManager;
    private String id, fullName, phoneNo, password, image, gender;
    private String previousActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        //declare variables
        mArrowBack = findViewById(R.id.back_update_pass);
        mTextView = findViewById(R.id.new_credentials_details);
        mCurrentPassword = findViewById(R.id.input_old_pass);
        mNewPassword = findViewById(R.id.input_new_pass);
        mConfirmPassword = findViewById(R.id.input_confirm_pass);
        mUpdateButton = findViewById(R.id.btn_update_pass);
        mCancelButton = findViewById(R.id.update_pass_cancel);

        //Get all the data from Intent
        previousActivity = getIntent().getStringExtra("activity");

        //Get user data from session
        sessionManager = new SessionManager(UpdatePasswordActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUserDetailsFromSession();

        id = usersDetails.get(SessionManager.KEY_ID);
        fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
        phoneNo = usersDetails.get(SessionManager.KEY_PHONENO);
        password = usersDetails.get(SessionManager.KEY_PASSWORD);
        image = usersDetails.get(SessionManager.KEY_IMAGE);
        gender = usersDetails.get(SessionManager.KEY_GENDER);

        checkPreviousActivity();
        callPreviousScreen();
        callForgetPasswordNextScreen();
        updatePassword();
    }

    private void checkPreviousActivity() {
        if (previousActivity.equals("Settings")) {
            mCurrentPassword.setVisibility(View.VISIBLE);
            mTextView.setVisibility(View.INVISIBLE);
        } else if (previousActivity.equals("ForgetPassword")) {
            mCurrentPassword.setVisibility(View.INVISIBLE);
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    private void updatePassword() {
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validate fields
                if (!checkCurrentPassword() | !validatePassword() | !validateConfirmPassword()) {
                    return;
                } //Validation succeeded and now move to next step

                if (!password.equals(mNewPassword.getEditText().getText().toString())) {
                    Call<UserResponse> updatePasswordCall = UsersApiClient.getService().editPassword(phoneNo,
                    mNewPassword.getEditText().getText().toString(), UsersApiClient.API_KEY);
                    updatePasswordCall.enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(UpdatePasswordActivity.this, UpdatePasswordSuccessMessage.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            Toast.makeText(UpdatePasswordActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(UpdatePasswordActivity.this, "Password is same and can't be updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkCurrentPassword() {
        String val = Objects.requireNonNull(mCurrentPassword.getEditText()).getText().toString().trim();

        if (mCurrentPassword.getVisibility() == View.VISIBLE) {
            if (val.isEmpty()) {
                mCurrentPassword.setError("Field can not be empty!");
                return false;
            } else if (!val.matches(password)) {
                mCurrentPassword.setError("Password not correct!");
                return false;
            } else {
                mCurrentPassword.setError(null);
                mCurrentPassword.setErrorEnabled(false);
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        String val = Objects.requireNonNull(mNewPassword.getEditText()).getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$";

        if (val.isEmpty()) {
            mNewPassword.setError("Field can not be empty!");
            return false;
        } else if (!val.matches(checkPassword)) {
            mNewPassword.setError("Password should contain 8 characters!");
            return false;
        } else {
            mNewPassword.setError(null);
            mNewPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String val = Objects.requireNonNull(mConfirmPassword.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(mNewPassword.getEditText()).getText().toString().trim();


        if (val.isEmpty()) {
            mConfirmPassword.setError("Field can not be empty!");
            return false;
        } else if (!val.matches(password)) {
            mConfirmPassword.setError("Passwords do not match!");
            return false;
        } else {
            mConfirmPassword.setError(null);
            mConfirmPassword.setErrorEnabled(false);
            return true;
        }
    }

    private void callForgetPasswordNextScreen() {
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdatePasswordActivity.this, UpdatePasswordSuccessMessage.class);
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
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdatePasswordActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}