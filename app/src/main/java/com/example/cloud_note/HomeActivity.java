package com.example.cloud_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private TextView tvLogin;
    private RecyclerView rcvListUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        tvLogin = (TextView) findViewById(R.id.tv_login);
        rcvListUser = (RecyclerView) findViewById(R.id.rcv_listUser);
        tvLogin.setOnClickListener(view->{
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}