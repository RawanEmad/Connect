package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.users.api.UsersApiClient;
import com.example.connect.users.model.UserListResponse;
import com.example.connect.users.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactProfileActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    private TextView phoneNoTextView;
    private TextView prefixTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        fullNameTextView = findViewById(R.id.contact_name_text_view);
        phoneNoTextView = findViewById(R.id.contact_phone_text_view);
        prefixTextView = findViewById(R.id.contact_prefix);

        getUser();
    }

    private void getUser() {
        Call<UserResponse> userResponseCall = UsersApiClient.getService().getUser("201066923650", UsersApiClient.API_KEY);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    String userName = response.body().getUserData().getFullName();
                    char firstLetter = userName.charAt(0);
                    String prefix = String.valueOf(firstLetter).toUpperCase();
                    String phoneNo = "+" + response.body().getUserData().getPhoneNo();
                    fullNameTextView.setText(userName);
                    phoneNoTextView.setText(phoneNo);
                    prefixTextView.setText(prefix);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ContactProfileActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}