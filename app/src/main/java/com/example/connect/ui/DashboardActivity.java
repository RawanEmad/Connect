package com.example.connect.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.users.network.UsersApiClient;
import com.example.connect.users.response.UserResponse;
import com.example.connect.utilities.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    //variables
    private TextView mUserText;
    private Button mRoomsButton, mContactsButton, mChatsButton;

    private SessionManager sessionManager;
    private String fullName, phoneNo, fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mUserText = findViewById(R.id.dashboard_text_view);
        mRoomsButton = findViewById(R.id.rooms_btn);
        mContactsButton = findViewById(R.id.contacts_btn);
        mChatsButton = findViewById(R.id.chats_btn);

        sessionManager = new SessionManager(DashboardActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUserDetailsFromSession();

        fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
        phoneNo = usersDetails.get(SessionManager.KEY_PHONENO);

        mUserText.setText("Hi "+ fullName);

        //Firebase app check
        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        getToken();
        callRoomsScreen();
        callContactsScreen();
        callChatsScreen();
    }

    private void getToken() {
        //Firebase cloud messaging
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log token
                        Log.d("FCM", "FCM Token: " + token);
                        updateToken(token);
                    }
                });
    }

    private void updateToken(String token) {
        Call<UserResponse> updatePasswordCall = UsersApiClient.getService().updateToken(phoneNo,
                token, UsersApiClient.API_KEY);
        updatePasswordCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("FCM", "FCM Token uploaded: " + response.body().getUserData().getFcmToken());
                    sessionManager.updateUserToken(token);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callChatsScreen() {
        mChatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, RecentConversationsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callContactsScreen() {
        mContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callRoomsScreen() {
        mRoomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, JoinMeetingActivity.class);
                startActivity(intent);
            }
        });
    }
}