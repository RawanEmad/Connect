package com.example.connect.users.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.connect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class StartUpScreenActivity extends AppCompatActivity {

    //variables
    private Button mLoginButton, mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_screen);

        //declare variables
        mLoginButton = findViewById(R.id.login_btn);
        mRegisterButton = findViewById(R.id.register_btn);

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

                        // Log and toast
                        Log.d("FCM", "Token: " + token);
                        //Toast.makeText(StartUpScreenActivity.this, "Token: " + token, Toast.LENGTH_SHORT).show();
                    }
                });

        callLoginScreen();
        callRegisterScreen();
    }

    private void callLoginScreen() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartUpScreenActivity.this, LoginActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair(findViewById(R.id.login_btn), "transition_login");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartUpScreenActivity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }

    private void callRegisterScreen() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartUpScreenActivity.this, RegisterActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair(findViewById(R.id.register_btn), "transition_register");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartUpScreenActivity.this, pairs);
                startActivity(intent, options.toBundle());            }
        });
    }
}