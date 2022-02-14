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
import com.example.connect.users.model.UserResponse;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersAdapterViewHolder> {

    private List<UserResponse> mUserResponseList;
    private Context mContext;

    public UsersAdapter() {
    }

    public void setData(List<UserResponse> userResponseList) {
        this.mUserResponseList = userResponseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new UsersAdapterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.users_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapterViewHolder holder, int position) {
        UserResponse userResponse = mUserResponseList.get(position);

        String userName = userResponse.getStatus();
        String prefix = userResponse.getLength();

        holder.prefix.setText(prefix);
        holder.userName.setText(userName);
    }

    @Override
    public int getItemCount() {
        return mUserResponseList.size();
    }

    public class UsersAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView prefix;
        ImageView moreInfo;

        public UsersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_image);
            prefix = itemView.findViewById(R.id.prefix);
            moreInfo = itemView.findViewById(R.id.more_details_image);
        }
    }
}
