package com.example.connect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connect.R;
import com.example.connect.users.network.UsersApiClient;
import com.example.connect.users.response.UserResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    //variables
    private ImageView mArrowBack;
    private Button mNextButton;

    private TextInputLayout mForgetPassInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //declare variables
        mArrowBack = findViewById(R.id.back_forget_pass);
        mNextButton = findViewById(R.id.forget_pass_next_btn);
        mForgetPassInput = findViewById(R.id.forget_pass_input_phone);

        callLoginScreen();
        callForgetPasswordNextScreen();
    }

    private boolean validatePhoneNumber() {
        String phoneNo = Objects.requireNonNull(mForgetPassInput.getEditText()).getText().toString().trim();

        if (phoneNo.isEmpty()) {
            mForgetPassInput.setError("Field can not be empty!");
            return false;
        } else {
            mForgetPassInput.setError(null);
            mForgetPassInput.setErrorEnabled(false);
            return true;
        }
    }

    private void checkIfUserExists() {
        //Validate fields
        if (!validatePhoneNumber()) {
            return;
        } //Validation succeeded and now move to next step

        String phoneNo = mForgetPassInput.getEditText().getText().toString();
        Call<UserResponse> userResponseCall = UsersApiClient.getService().getUser(phoneNo, UsersApiClient.API_KEY);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(ForgetPasswordActivity.this, OtpVerificationActivity.class);
                    //Pass all fields to the next activity
                    intent.putExtra("phoneNo", phoneNo);
                    intent.putExtra("activity", "ForgetPassword");
                    startActivity(intent);
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Phone Number doesn't exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ForgetPasswordActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callForgetPasswordNextScreen() {
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIfUserExists();
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