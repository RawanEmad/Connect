package com.example.connect.users;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //variables
    SharedPreferences usersSession;
    SharedPreferences.Editor mEditor;
    Context mContext;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID = "userId";
    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_PHONENO = "phoneNo";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_IMAGE = "profileImage";
    public static final String KEY_GENDER = "gender";

    public SessionManager(Context context) {
        mContext = context;
        usersSession = mContext.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        mEditor = usersSession.edit();
    }

    public void createLoginSession(String userId, String fullName, String phoneNo, String password, String profileImage, String gender) {

        mEditor.putBoolean(IS_LOGIN, true);

        mEditor.putString(KEY_ID, userId);
        mEditor.putString(KEY_FULLNAME, fullName);
        mEditor.putString(KEY_PHONENO, phoneNo);
        mEditor.putString(KEY_PASSWORD, password);
        mEditor.putString(KEY_IMAGE, profileImage);
        mEditor.putString(KEY_GENDER, gender);

        mEditor.commit();
    }

    public HashMap<String, String> getUsersDetailsFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_ID, usersSession.getString(KEY_ID, null));
        userData.put(KEY_FULLNAME, usersSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_PHONENO, usersSession.getString(KEY_PHONENO, null));
        userData.put(KEY_PASSWORD, usersSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_IMAGE, usersSession.getString(KEY_IMAGE, null));
        userData.put(KEY_GENDER, usersSession.getString(KEY_GENDER, null));

        return userData;
    }

    public boolean checkLogin() {
        if(usersSession.getBoolean(IS_LOGIN, true)){
            return true;
        } else
            return false;
    }

    public void logoutUserFromSession() {
        mEditor.clear();
        mEditor.commit();
    }

}
