package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.connect.R;
import com.example.connect.users.SessionManager;

import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    //variables
    private ImageView mBackButton;
    private TextView mUserText;
    private Button mRoomsButton, mContactsButton, mLogoutButton;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mBackButton = findViewById(R.id.dashboard_back_btn);
        mUserText = findViewById(R.id.dashboard_text_view);
        mRoomsButton = findViewById(R.id.rooms_btn);
        mContactsButton = findViewById(R.id.contacts_btn);
        mLogoutButton = findViewById(R.id.logout_btn);

        sessionManager = new SessionManager(DashboardActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUserDetailsFromSession();

        String fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
        String phoneNo = usersDetails.get(SessionManager.KEY_PHONENO);

        mUserText.setText("Hi "+ fullName);

        callPreviousScreen();
        callRoomsScreen();
        callContactsScreen();
        //logoutUser();
    }

    private void logoutUser() {
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SessionManager sessionManager = new SessionManager(DashboardActivity.this, SessionManager.SESSION_USERSESSION);
                sessionManager.logoutFromUserSession();
                Intent intent = new Intent(DashboardActivity.this, StartUpScreenActivity.class);
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

    private void callPreviousScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}