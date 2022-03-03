package com.example.connect.listeners;

import com.example.connect.users.model.UserModel;

public interface UsersListeners {

    void initiateVideoMeeting(UserModel userModel);

    void initiateAudioMeeting(UserModel userModel);
}
