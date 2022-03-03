package com.example.connect.users;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect.R;
import com.example.connect.ui.ContactProfileActivity;
import com.example.connect.users.model.UserModel;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private ArrayList<UserModel> mUserResponseList;

    public UsersAdapter(ArrayList<UserModel> userResponseList) {
        mUserResponseList = userResponseList;
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

        String id = userResponse.getUserId();
        String userName = userResponse.getFullName();
        char firstLetter = userName.charAt(0);
        String prefix = String.valueOf(firstLetter).toUpperCase();
        String phoneNo = userResponse.getPhoneNo();
        String gender = userResponse.getGender();

        holder.getPrefix().setText(prefix);
        holder.getUserName().setText(userName);
        holder.getMoreInfo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ContactProfileActivity.class);
                //Pass all fields to the next activity
                intent.putExtra("id", id);
                intent.putExtra("fullName", userName);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("prefix", prefix);
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
        TextView prefix;
        ImageView moreInfo;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            prefix = itemView.findViewById(R.id.prefix);
            moreInfo = itemView.findViewById(R.id.more_details_image);

        }

        public TextView getUserName() {
            return userName;
        }

        public TextView getPrefix() {
            return prefix;
        }

        public ImageView getMoreInfo() {
            return moreInfo;
        }
    }
}
