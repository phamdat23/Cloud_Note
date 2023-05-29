package com.example.cloud_note;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloud_note.APIs.APINote;
import com.example.cloud_note.DAO.Login;
import com.example.cloud_note.Model.LoginModel;
import com.example.cloud_note.Model.Model_State_Login;
import com.example.cloud_note.Model.POST.LoginReq;
import com.google.android.material.textfield.TextInputLayout;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout inputUsername;
    private TextInputLayout inputPasswd;
    private TextView tvSignUp;
    private Button btnLogin;
    LoginModel loginModelm;
    Login daoLogin;
    Model_State_Login user;

    KProgressHUD hud;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputUsername = (TextInputLayout) findViewById(R.id.input_username);
        inputPasswd = (TextInputLayout) findViewById(R.id.input_passwd);
        tvSignUp = (TextView) findViewById(R.id.tv_signUp);
        btnLogin = (Button) findViewById(R.id.btn_login);
        daoLogin = new Login(LoginActivity.this);
        user = daoLogin.getLogin();
        hud = KProgressHUD.create(LoginActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputUsername.getEditText().getText().toString() != "" && inputPasswd.getEditText().getText().toString() != "") {
                    inputPasswd.setError("");
                    inputUsername.setError("");
                    login();
                } else {
                    if (inputUsername.getEditText().getText().toString() != "") {
                        inputUsername.setError("Trường này không được để trống");
                    } else {
                        inputUsername.setError("");
                    }
                    if (inputPasswd.getEditText().getText().toString() != "") {
                        inputPasswd.setError("Password không được để trống");
                    } else {
                        inputPasswd.setError("");
                    }
                }

            }
        });


    }

    public void login() {
        hud.show();
        LoginReq loginReq = new LoginReq(inputUsername.getEditText().getText().toString(), inputPasswd.getEditText().getText().toString());
        APINote.apiService.login(loginReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull LoginModel loginModel) {
                        loginModelm = loginModel;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hud.dismiss();
                        Log.e("TAG", "onError: " + e);
                        inputPasswd.setError("Sai mật khẩu");
                    }

                    @Override
                    public void onComplete() {
                        if (loginModelm.getStatus() == 200) {
                            long res = daoLogin.insert(new Model_State_Login(loginModelm.getUser().getId(), loginModelm.getJwt(), 0));
                            if (res > 0) {
                                hud.dismiss();
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

    }

    @Override
    public void onBackPressed() {

        if (user.getIdUer() == 0) {
            Intent intent = new Intent(LoginActivity.this, LoadingPageActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }


    }
}