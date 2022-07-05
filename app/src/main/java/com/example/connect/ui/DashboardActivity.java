package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.connect.R;
import com.example.connect.utilities.SessionManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;

import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    //variables
    private TextView mUserText;
    private Button mRoomsButton, mContactsButton, mChatsButton;

    private SessionManager sessionManager;

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

        String fullName = usersDetails.get(SessionManager.KEY_FULLNAME);

        mUserText.setText("Hi "+ fullName);

        //Firebase app check
        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        callRoomsScreen();
        callContactsScreen();
        callChatsScreen();
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