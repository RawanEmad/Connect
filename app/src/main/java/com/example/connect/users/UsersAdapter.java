package com.example.connect.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect.R;
import com.example.connect.users.model.UserListResponse;
import com.example.connect.users.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private ArrayList<UserModel> mUserResponseList;

    public UsersAdapter(ArrayList<UserModel> userResponseList) {
        mUserResponseList = userResponseList;
    }

    public void setData(ArrayList<UserModel> userResponseList) {
        this.mUserResponseList = userResponseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list_item, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        UserModel userResponse = mUserResponseList.get(position);

        String userName = userResponse.getFullName();
        String prefix = userResponse.getPhoneNo();

        //holder.getPrefix().setText(prefix);
        holder.getUserName().setText(userName);
//        holder.getContactImage().setImageResource(R.drawable.user_background);
//        holder.getMoreInfo().setImageResource(R.drawable.ic_baseline_info_24);
    }

    @Override
    public int getItemCount() {
        return mUserResponseList.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView prefix;
//        ImageView contactImage;
//        ImageView moreInfo;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
//            prefix = itemView.findViewById(R.id.prefix);
//            contactImage = itemView.findViewById(R.id.contact_profile_image);
//            moreInfo = itemView.findViewById(R.id.more_details_image);
        }

        public TextView getUserName() {
            return userName;
        }

//        public TextView getPrefix() {
//            return prefix;
//        }

//        public ImageView getContactImage() {
//            return contactImage;
//        }
//
//        public ImageView getMoreInfo() {
//            return moreInfo;
//        }
    }
}
