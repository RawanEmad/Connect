package com.example.connect.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.connect.R;
import com.example.connect.users.network.UsersApiClient;
import com.example.connect.users.response.UserResponse;
import com.example.connect.utilities.SessionManager;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private TextView mFullName, mPhoneNo;
    private CircleImageView mProfileImage;
    private ImageView mBackButton;
    private FrameLayout mEditProfile, mEditPassword, mNotifications, mDeleteAccount, mLogout;

    private SessionManager sessionManager;
    private String fullName,phoneNo, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mBackButton = findViewById(R.id.profile_back_btn);
        mProfileImage = findViewById(R.id.user_profile_image);
        mFullName = findViewById(R.id.user_name_text_view);
        mPhoneNo = findViewById(R.id.user_phone_text_view);
        mEditProfile = findViewById(R.id.edit_profile_layout);
        mEditPassword = findViewById(R.id.edit_pass_layout);
        mNotifications = findViewById(R.id.notifications_layout);
        mDeleteAccount = findViewById(R.id.delete_account_layout);
        mLogout = findViewById(R.id.logout_layout);

        displayUserData();
        callPreviousScreen();
        callEditProfileScreen();
        callEditPasswordScreen();
        callDeleteUserAccount();
        callLogoutUser();
    }

    private void deleteUserAccount() {
        Call<UserResponse> userResponseCall = UsersApiClient.getService().deleteUser(phoneNo, UsersApiClient.API_KEY);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Toast.makeText(SettingsActivity.this, "Deleted Account successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(SettingsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callDeleteUserAccount() {
        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("Are you sure you want to delete account?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteUserAccount();
                                Toast.makeText(SettingsActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void logOutUser() {
        Call<UserResponse> updatePasswordCall = UsersApiClient.getService().updateToken(phoneNo,
                "no", UsersApiClient.API_KEY);
        updatePasswordCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    sessionManager.logoutFromUserSession();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(SettingsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callLogoutUser() {
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                logOutUser();
                                Toast.makeText(SettingsActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void callEditPasswordScreen() {
        mEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, UpdatePasswordActivity.class);
                //Pass all fields to the next activity
                intent.putExtra("activity", "Settings");
                startActivity(intent);
            }
        });
    }

    private void callEditProfileScreen() {
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callPreviousScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayUserData() {
        sessionManager = new SessionManager(SettingsActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUserDetailsFromSession();

        fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
        phoneNo = usersDetails.get(SessionManager.KEY_PHONENO);
        image = usersDetails.get(SessionManager.KEY_IMAGE);

        mFullName.setText(fullName);
        mPhoneNo.setText(phoneNo);

        //Adding Glide library to display images
        Glide.with(getApplicationContext())
                .load(image)
                .into(mProfileImage);
    }
}