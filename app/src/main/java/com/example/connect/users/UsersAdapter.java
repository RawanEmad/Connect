package com.example.connect.users;

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
import com.example.connect.users.model.UserModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private ArrayList<UserModel> mUserResponseList;
    private Context mContext;

    public UsersAdapter(ArrayList<UserModel> userResponseList) {
        mUserResponseList = userResponseList;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.users_list_item, parent, false);
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

        holder.getUserName().setText(userName);

        //Adding Glide library to display images
        Glide.with(mContext)
                .load(profileImage)
                .into(holder.profileImage);

        holder.getMoreInfo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ContactProfileActivity.class);
                //Pass all fields to the next activity
                intent.putExtra("id", id);
                intent.putExtra("fullName", userName);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("image", profileImage);
                intent.putExtra("gender", gender);
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
