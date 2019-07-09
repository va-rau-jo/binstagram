package com.example.binstagram.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.binstagram.R;
import com.example.binstagram.utils.SignupHelper;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.etSignupUsername)
    EditText etUsername;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etSignupPassword)
    EditText etPassword;

    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    @BindView(R.id.btnSignUp)
    Button btnSignUp;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        setActionBar(toolbar);
        if(getActionBar() != null)
            getActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.btnSignUp)
    public void signUp() {
        ParseUser user = new ParseUser();

        if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            Toast.makeText(this, "Passwords must match!", Toast.LENGTH_LONG).show();
        } else if(!SignupHelper.isEmailValid(etEmail.getText().toString())) {
            Toast.makeText(this, "Email must be valid!", Toast.LENGTH_LONG).show();
        } else {
            user.setUsername(etUsername.getText().toString());
            user.setPassword(etPassword.getText().toString());
            user.setEmail(etEmail.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(SignupActivity.this, "Signup was successful!",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        // TODO: More failure messages, username taken, email taken, etc
                        Toast.makeText(SignupActivity.this, "Signup failed",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
