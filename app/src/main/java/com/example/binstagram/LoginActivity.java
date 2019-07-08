package com.example.binstagram;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.etUsername)
    EditText etUsername;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.app_bar)
    Toolbar toolbar;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setActionBar(toolbar);
        if(getActionBar() != null)
            getActionBar().setDisplayShowTitleEnabled(false);

    }

    @OnClick(R.id.btnLogin)
    public void setLoginOnClick() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        login(username, password);
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null) {
                    Log.d("here", "lets go gaosdfasdfsd");
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Log.d("LoginActivity Error", "asdfasdfsdf");
                    e.printStackTrace();
                }
            }
        });
    }
}
