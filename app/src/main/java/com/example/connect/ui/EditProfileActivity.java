package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.connect.R;
import com.example.connect.utilities.SessionManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView mBackButton;
    private CircleImageView mProfileImage;
    private TextInputLayout mFullName, mPhoneNo;
    private AutoCompleteTextView mGender;

    private SessionManager sessionManager;

    String[] genderListItems = {"Male","Female"};
    ArrayAdapter<String> mArrayAdapter;
    private String _gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //declare variables
        mBackButton = findViewById(R.id.profile_back_btn);

        //declare data variables
        mFullName = findViewById(R.id.register_input_full_name);
        mPhoneNo = findViewById(R.id.register_input_phone);
        mGender = findViewById(R.id.input_gender);
        mProfileImage = findViewById(R.id.user_profile_image);

        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.gender_list_items, genderListItems);

        mArrayAdapter.setDropDownViewResource(R.layout.gender_list_items);
        mGender.setAdapter(mArrayAdapter);

        mGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                _gender = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(adapterView.getContext(), "You selected: " + _gender, Toast.LENGTH_SHORT).show();
            }
        });

        callPreviousScreen();
        displayUserData();
    }

    private void callPreviousScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayUserData() {
        sessionManager = new SessionManager(EditProfileActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUserDetailsFromSession();

        String fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
        String phoneNo = usersDetails.get(SessionManager.KEY_PHONENO);
        String image = usersDetails.get(SessionManager.KEY_IMAGE);
        String gender = usersDetails.get(SessionManager.KEY_GENDER);

        mFullName.getEditText().setText(fullName);
        mPhoneNo.getEditText().setText(phoneNo);
        mGender.setText(gender);

        //Adding Glide library to display images
        Glide.with(getApplicationContext())
                .load(image)
                .into(mProfileImage);
    }
}