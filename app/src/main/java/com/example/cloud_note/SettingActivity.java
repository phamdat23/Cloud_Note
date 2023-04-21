package com.example.cloud_note;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cloud_note.DAO.Login;
import com.example.cloud_note.Model.Model_State_Login;

public class SettingActivity extends AppCompatActivity {
    private ImageView imgBack;
    private Button btnLogout;
    Login daoLogin;
    Model_State_Login user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        imgBack = (ImageView) findViewById(R.id.img_back);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        daoLogin = new Login(SettingActivity.this);
        user = daoLogin.getLogin();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_Logout();
            }
        });
    }

    private void dialog_Logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("SIGN OUT");
        builder.setMessage("Do you want to sign out of this account" );
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int res = daoLogin.delete(user);
                if (res > 0) {
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}