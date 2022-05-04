package com.example.connect.listeners;

import com.example.connect.models.UserModel;

public interface UsersListeners {

    void initiateVideoMeeting(UserModel userModel);

    void initiateAudioMeeting(UserModel userModel);
}
