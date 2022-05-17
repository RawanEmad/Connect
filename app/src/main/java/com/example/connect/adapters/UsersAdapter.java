package com.example.connect.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.connect.R;
import com.example.connect.ui.ContactProfileActivity;
import com.example.connect.utilities.Constants;
import com.example.connect.utilities.SessionManager;
import com.example.connect.models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private ArrayList<UserModel> mUserResponseList;
    private Context mContext;
    private String currentUserId;
    private SessionManager sessionManager;

    public UsersAdapter(ArrayList<UserModel> userResponseList) {
        mUserResponseList = userResponseList;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        sessionManager = new SessionManager(mContext, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUserDetailsFromSession();

        currentUserId = usersDetails.get(SessionManager.KEY_ID);
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_container_users, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        UserModel userResponse = mUserResponseList.get(position);

        String id = userResponse.getUserId();
        String userName = userResponse.getFullName();
        String profileImage = userResponse.getProfileImage();
        String phoneNo = userResponse.getPhoneNo();
        String gender = userResponse.getGender();

        if (currentUserId.equals(id)) {
            holder.getUserName().setVisibility(View.GONE);
            holder.profileImage.setVisibility(View.GONE);
            holder.getMoreInfo().setVisibility(View.GONE);
        }
        holder.getUserName().setText(userName);

        //Adding Glide library to display images
        Glide.with(mContext)
                .load(profileImage)
                .into(holder.profileImage);

        holder.getMoreInfo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.KEY_ID = id;
                Constants.KEY_FULL_NAME = userName;
                Constants.KEY_PHONE_NO = phoneNo;
                Constants.KEY_IMAGE = profileImage;
                Constants.KEY_GENDER = gender;

                Intent intent = new Intent(view.getContext(), ContactProfileActivity.class);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUserResponseList.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        CircleImageView profileImage;
        ImageView moreInfo;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            profileImage = itemView.findViewById(R.id.user_image);
            moreInfo = itemView.findViewById(R.id.more_details_image);

        }

        public TextView getUserName() {
            return userName;
        }

        public ImageView getMoreInfo() {
            return moreInfo;
        }
    }
}
