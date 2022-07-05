package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.connect.R;
import com.example.connect.users.network.UsersApiClient;
import com.example.connect.users.response.UserResponse;
import com.example.connect.utilities.SessionManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView mBackButton;
    private CircleImageView mProfileImage;
    private TextInputLayout mFullName, mPhoneNo;
    private AutoCompleteTextView mGender;
    private Button mUpdateButton, mCancelButton;
    private FloatingActionButton mFloatingActionButton;

    private SessionManager sessionManager;
    private Uri selectedfile;

    private String id, fullName, phoneNo, password, image, gender;
    private String newFullName, newPhoneNo, newProfileImage, encodedImage, newGender;
    private final HashMap userMap = new HashMap();

    String[] genderListItems = {"Male", "Female"};
    ArrayAdapter<String> mArrayAdapter;
    private String _gender;

    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_MEDIA_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //declare variables
        mBackButton = findViewById(R.id.profile_back_btn);
        mUpdateButton = findViewById(R.id.update_btn);
        mCancelButton = findViewById(R.id.edit_profile_cancel);

        //declare data variables
        mFullName = findViewById(R.id.register_input_full_name);
        mPhoneNo = findViewById(R.id.register_input_phone);
        mGender = findViewById(R.id.input_gender);
        mProfileImage = findViewById(R.id.user_profile_image);
        mFloatingActionButton = findViewById(R.id.fab_edit);

        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.gender_list_items, genderListItems);

        mArrayAdapter.setDropDownViewResource(R.layout.gender_list_items);
        mGender.setAdapter(mArrayAdapter);

        mGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                _gender = adapterView.getItemAtPosition(position).toString();
            }
        });

        callPreviousScreen();
        displayUserData();
        editProfileImage();
        updateUserData();
    }

    private void callPreviousScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
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

        id = usersDetails.get(SessionManager.KEY_ID);
        fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
        phoneNo = usersDetails.get(SessionManager.KEY_PHONENO);
        password = usersDetails.get(SessionManager.KEY_PASSWORD);
        image = usersDetails.get(SessionManager.KEY_IMAGE);
        gender = usersDetails.get(SessionManager.KEY_GENDER);

        mFullName.getEditText().setText(fullName);
        mPhoneNo.getEditText().setText(phoneNo);
        mGender.setText(gender);

        //Adding Glide library to display images
        Glide.with(getApplicationContext())
                .load(image)
                .into(mProfileImage);
    }

    private void updateUserSession() {
        if (isProfileImageChanged()) {
            image = newProfileImage;
        }
        if (isNameChanged()) {
            fullName = newFullName;
        }
        if (isPhoneChanged()) {
            phoneNo = newPhoneNo;
        }
        if (isGenderChanged()) {
            gender = newGender;
        }
        //create login session
        sessionManager = new SessionManager(EditProfileActivity.this, SessionManager.SESSION_USERSESSION);
        sessionManager.createLoginSession(id, fullName, phoneNo, password, image, gender);
    }

    private void updateUserData() {
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isProfileImageChanged() || isNameChanged() || isPhoneChanged() || isGenderChanged()) {
                    Call<UserResponse> editUserCall = UsersApiClient.getService().editUser(phoneNo,
                            userMap, UsersApiClient.API_KEY);
                    editUserCall.enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            if (response.isSuccessful()) {
                                updateUserSession();
                                Toast.makeText(EditProfileActivity.this, "Data has been updated", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            Toast.makeText(EditProfileActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(EditProfileActivity.this, "Data is same and can't be updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isProfileImageChanged() {
        if (newProfileImage != null) {
            userMap.put("image", newProfileImage);
            return true;
        } else {
            return false;
        }
    }

    private boolean isNameChanged() {
        if (!fullName.equals(mFullName.getEditText().getText().toString())) {
            newFullName = mFullName.getEditText().getText().toString();
            userMap.put("name", newFullName);
            return true;
        } else {
            return false;
        }
    }

    private boolean isPhoneChanged() {
        if (!phoneNo.equals(mPhoneNo.getEditText().getText().toString())) {
            newPhoneNo = mPhoneNo.getEditText().getText().toString();
            userMap.put("mobile", newPhoneNo);
            return true;
        } else {
            return false;
        }
    }

    private boolean isGenderChanged() {
        if (!gender.equals(mGender.getText().toString())) {
            newGender = mGender.getText().toString();
            userMap.put("gender", newGender);
            return true;
        } else {
            return false;
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void editProfileImage() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(EditProfileActivity.this);
                ImagePicker.Companion.with(EditProfileActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(620)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1024, 1024)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        selectedfile = Objects.requireNonNull(data).getData();

        if (selectedfile != null) {
            mProfileImage.setImageURI(selectedfile);
            newProfileImage = selectedfile.toString();

            Log.d("Encoded string", "Encoded string: " + newProfileImage);
        }

    }
}