package com.example.connect.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.connect.R;
import com.example.connect.models.UserModel;
import com.example.connect.users.request.RegisterRequest;
import com.example.connect.users.network.UsersApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationActivity extends AppCompatActivity {

    //variables
    private ImageView mCloseButton;
    private Button mVerifyButton;

    //data variables
    private PinView mPinView;
    private String codeBySystem;
    String fullName, phoneNo, password, gender, previousActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        //declare variables
        mCloseButton = findViewById(R.id.otp_close_btn);
        mVerifyButton = findViewById(R.id.verify_btn);
        TextView userPhoneText = findViewById(R.id.phone_no_text_view);

        //declare data variables
        mPinView = findViewById(R.id.pin_view);

        previousActivity = getIntent().getStringExtra("activity");

        //Get all the data from Intent
        if (previousActivity.equals("ForgetPassword")) {
            phoneNo = getIntent().getStringExtra("phoneNo");
        } else if (previousActivity.equals("Register")) {
            fullName = getIntent().getStringExtra("fullName");
            phoneNo = getIntent().getStringExtra("phoneNo");
            password = getIntent().getStringExtra("password");
            gender = getIntent().getStringExtra("gender");
        }

        String completePhoneNo = "+" + phoneNo;

        userPhoneText.setText(completePhoneNo);

        callRegisterScreen();
        callMainScreen();
        sendVerificationCodeToUser(completePhoneNo);
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        mPinView.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(OtpVerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Verification completed successfully here Either
                            //store the data or do whatever desire
                            if (previousActivity.equals("ForgetPassword")) {
                                Intent intent = new Intent(OtpVerificationActivity.this, UpdatePasswordActivity.class);
                                intent.putExtra("activity", "ForgetPassword");
                                startActivity(intent);
                                finish();
                            } else if (previousActivity.equals("Register")) {
                                RegisterRequest registerRequest = new RegisterRequest();
                                registerRequest.setFullName(fullName);
                                registerRequest.setPhoneNo(phoneNo);
                                registerRequest.setPassword(password);
                                registerRequest.setGender(gender);

                                registerUser(registerRequest);
                            }

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(OtpVerificationActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OtpVerificationActivity.this, "Verification Not Completed! Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void registerUser(RegisterRequest registerRequest) {
        Call<UserModel> registerResponseCall = UsersApiClient.getService().registerUser(registerRequest, UsersApiClient.API_KEY);
        registerResponseCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OtpVerificationActivity.this, "Successful Registration", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OtpVerificationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(OtpVerificationActivity.this, "Registration Not Completed! Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(OtpVerificationActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callRegisterScreen() {
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtpVerificationActivity.this, StartUpScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callMainScreen() {
        mVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = Objects.requireNonNull(mPinView.getText()).toString();
                if (!code.isEmpty()) {
                    verifyCode(code);
                }
            }
        });
    }
}