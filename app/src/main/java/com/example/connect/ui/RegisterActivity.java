package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.users.network.UsersApiClient;
import com.example.connect.users.response.UserResponse;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    String[] genderListItems = {"Male","Female"};
    ArrayAdapter<String> mArrayAdapter;

    //variables
    private ImageView mBackButton;
    private Button mRegisterButton;
    private TextView mLoginButton;

    //data variables
    private TextInputLayout mFullName, mPhoneNumber, mPassword, mConfirmPassword;
    private CountryCodePicker mCountryCodePicker;
    private AutoCompleteTextView mGender;
    private String _gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //declare variables
        mBackButton = findViewById(R.id.register_back_btn);
        mRegisterButton = findViewById(R.id.register_btn);
        mLoginButton = findViewById(R.id.login_text_view);

        //declare data variables
        mFullName = findViewById(R.id.register_input_full_name);
        mPhoneNumber = findViewById(R.id.register_input_phone);
        mCountryCodePicker = findViewById(R.id.register_country_code_picker);
        mPassword = findViewById(R.id.register_input_password);
        mConfirmPassword = findViewById(R.id.register_input_confirm_password);
        mGender = findViewById(R.id.input_gender);

        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.gender_list_items, genderListItems);

        mArrayAdapter.setDropDownViewResource(R.layout.gender_list_items);
        mGender.setAdapter(mArrayAdapter);

        mGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                _gender = adapterView.getItemAtPosition(position).toString();
            }
        });

        callStartUpScreen();
        callOtpVerificationScreen();
        callLoginScreen();
    }

    private boolean validateFullName() {
        String val = Objects.requireNonNull(mFullName.getEditText()).getText().toString().trim();
        if (val.isEmpty()) {
            mFullName.setError("Field can not be empty!");
            return false;
        } else {
            mFullName.setError(null);
            mFullName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhoneNumber() {
        String val = Objects.requireNonNull(mPhoneNumber.getEditText()).getText().toString().trim();

        if (val.isEmpty()) {
            mPhoneNumber.setError("Field can not be empty!");
            return false;
        } else if (val.contains(" ")) {
            mPhoneNumber.setError("No White spaces are allowed!");
            return false;
        } else {
            mPhoneNumber.setError(null);
            mPhoneNumber.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = Objects.requireNonNull(mPassword.getEditText()).getText().toString().trim();
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
            mPassword.setError("Field can not be empty!");
            return false;
        } else if (!val.matches(checkPassword)) {
            mPassword.setError("Password should contain 8 characters!");
            return false;
        } else {
            mPassword.setError(null);
            mPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String val = Objects.requireNonNull(mConfirmPassword.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(mPassword.getEditText()).getText().toString().trim();


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
                //Validate fields
                if (!validateFullName() | !validatePhoneNumber() | !validatePassword() | !validateConfirmPassword()) {
                    return;
                } //Validation succeeded and now move to next screen

                String _fullName = mFullName.getEditText().getText().toString();
                String _password = mPassword.getEditText().getText().toString();

                //Get complete phone number
                String _getUserEnteredPhoneNumber = Objects.requireNonNull(mPhoneNumber.getEditText()).getText().toString().trim();
                //Remove first zero if entered!
                if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
                    _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
                }
                //Complete phone number
                String _phoneNo = mCountryCodePicker.getSelectedCountryCode()
                        + _getUserEnteredPhoneNumber;

                Intent intent = new Intent(RegisterActivity.this, OtpVerificationActivity.class);

                //Pass all fields to the next activity
                intent.putExtra("fullName", _fullName);
                intent.putExtra("phoneNo", _phoneNo);
                intent.putExtra("password", _password);
                intent.putExtra("gender", _gender);
                intent.putExtra("activity", "Register");

                Call<UserResponse> userResponseCall = UsersApiClient.getService().getUser(_phoneNo, UsersApiClient.API_KEY);
                userResponseCall.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Phone Number already exists!", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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