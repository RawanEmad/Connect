package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.users.UsersAdapter;
import com.example.connect.users.network.UsersApiClient;
import com.example.connect.users.model.UserListResponse;
import com.example.connect.users.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsActivity extends AppCompatActivity {

    //variables
    private ImageView mBackButton;
    private TextView mContactsLength;
    private RecyclerView mRecyclerView;

    private ArrayList<UserListResponse> mUserModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mBackButton = findViewById(R.id.contacts_back_btn);
        mContactsLength = findViewById(R.id.my_contacts_text);
        mRecyclerView = findViewById(R.id.users_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        callLoginScreen();

        getAllUsers();
    }

    private void callLoginScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getAllUsers() {
        Call<UserListResponse> usersList = UsersApiClient.getService().getAllUsers(UsersApiClient.API_KEY);
        usersList.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.isSuccessful()) {
                    List<UserModel> userListResponse = response.body().getUsersList();
                    //mUserModelList = userListResponse.getUsersList();
                    //mUserModelList = new ArrayList<>(Arrays.asList(userListResponse.getUsersList()));

                    for (int i=0; i<userListResponse.size(); i++) {
                        UsersAdapter usersAdapter = new UsersAdapter((ArrayList<UserModel>) userListResponse);
                        mRecyclerView.setAdapter(usersAdapter);
                        usersAdapter.notifyDataSetChanged();
                    }
                    //putDataIntoRecyclerView(mUserModelList);

                    //Toast.makeText(ContactsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();

                    mContactsLength.setText("My Contacts (" + response.body().getLength() + ")");
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                Toast.makeText(ContactsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void putDataIntoRecyclerView(ArrayList<UserListResponse> userModelList) {

    }

}