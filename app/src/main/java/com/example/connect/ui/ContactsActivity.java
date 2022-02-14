package com.example.connect.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.users.UsersAdapter;
import com.example.connect.users.api.UsersApiClient;
import com.example.connect.users.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsActivity extends AppCompatActivity {

    //variables
    private ImageView mBackButton;
    private TextView mContactsLength;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private UsersAdapter mUsersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mBackButton = findViewById(R.id.contacts_back_btn);
        mContactsLength = findViewById(R.id.my_contacts_text);
        mRecyclerView = findViewById(R.id.users_recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mUsersAdapter = new UsersAdapter();

        callLoginScreen();

        getAllUsers();
    }

    private void callLoginScreen() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, ContactProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getAllUsers() {
        Call<UserResponse> usersList = UsersApiClient.getService().getAllUsers(UsersApiClient.API_KEY);
        usersList.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
//                    List<UserResponse> userResponseList = (List<UserResponse>) response.body();
//                    mUsersAdapter.setData(userResponseList);
//                    mRecyclerView.setAdapter(mUsersAdapter);
                    Toast.makeText(ContactsActivity.this, "onResponse: " + response.body().toString(), Toast.LENGTH_SHORT).show();
                    mContactsLength.setText("My Contacts (" + response.body().getLength() + ")");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ContactsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}