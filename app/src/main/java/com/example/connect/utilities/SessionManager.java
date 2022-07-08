package com.example.connect.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //variables
    SharedPreferences usersSession;
    SharedPreferences.Editor mEditor;
    Context mContext;

    //Session names
    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMBERME = "rememberMe";

    //User Session variables
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID = "userId";
    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_PHONENO = "phoneNo";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_IMAGE = "profileImage";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_FCM_TOKEN = "fcmToken";

    //Remember Me variables
    private static final String IS_REMEMBERME = "IsRememberMe";

    public static final String KEY_SESSIONPHONENO = "phoneNo";
    public static final String KEY_SESSIONPASSWORD = "password";

    //constructor
    public SessionManager(Context context, String sessionName) {
        mContext = context;
        usersSession = mContext.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        mEditor = usersSession.edit();
    }

    /*
    Users
    Login Session
     */
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

    public void updateUserToken(String fcmToken) {
        mEditor.putString(KEY_FCM_TOKEN, fcmToken);
        mEditor.commit();
    }

    public HashMap<String, String> getUserDetailsFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_ID, usersSession.getString(KEY_ID, null));
        userData.put(KEY_FULLNAME, usersSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_PHONENO, usersSession.getString(KEY_PHONENO, null));
        userData.put(KEY_PASSWORD, usersSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_IMAGE, usersSession.getString(KEY_IMAGE, null));
        userData.put(KEY_GENDER, usersSession.getString(KEY_GENDER, null));
        userData.put(KEY_FCM_TOKEN, usersSession.getString(KEY_FCM_TOKEN, null));

        return userData;
    }

    public boolean checkLogin() {
        if(usersSession.getBoolean(IS_LOGIN, true) && !usersSession.getString(KEY_FCM_TOKEN, null).equals("no")){
            return true;
        } else
            return false;
    }

    public void logoutFromUserSession() {
        mEditor.clear();
        mEditor.commit();
    }

    /*
    Remember Me
    Session functions
     */
    public void createRememberMeSession(String phoneNo, String password) {

        mEditor.putBoolean(IS_REMEMBERME, true);

        mEditor.putString(KEY_SESSIONPHONENO, phoneNo);
        mEditor.putString(KEY_SESSIONPASSWORD, password);

        mEditor.commit();
    }

    public HashMap<String, String> getRememberMeDetailsFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_SESSIONPHONENO, usersSession.getString(KEY_SESSIONPHONENO, null));
        userData.put(KEY_SESSIONPASSWORD, usersSession.getString(KEY_SESSIONPASSWORD, null));

        return userData;
    }

    public boolean checkRememberMe() {
        if(usersSession.getBoolean(IS_REMEMBERME, false)){
            return true;
        } else
            return false;
    }

}
