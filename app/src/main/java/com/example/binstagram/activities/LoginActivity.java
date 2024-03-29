package com.example.binstagram.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.binstagram.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.btnGoToSignup)
    Button btnSignup;

    @BindView(R.id.etLoginUsername)
    EditText etUsername;

    @BindView(R.id.etLoginPassword)
    EditText etPassword;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Persist user
        if(ParseUser.getCurrentUser() != null)
            onSuccessfulLogin();
    }

    @OnClick(R.id.btnLogin)
    public void setLoginOnClick() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        login(username, password);
    }

    @OnClick(R.id.btnGoToSignup)
    public void goToSignupScreen() {
        startActivity(new Intent(this, SignupActivity.class));
    }

    private void login(String username, String password) {
        Log.d("here", "logging in");
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null) {
                    Log.d("here", "lets go gaosdfasdfsd");
                    onSuccessfulLogin();
                } else {
                    Log.d("LoginActivity Error", "asdfasdfsdf");
                    e.printStackTrace();
                }
            }
        });
    }

    private void onSuccessfulLogin() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }
}
