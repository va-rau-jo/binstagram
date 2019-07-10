package com.example.binstagram.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.binstagram.R;
import com.example.binstagram.utils.FileHelper;
import com.example.binstagram.utils.FormatHelper;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    // TODO: refactor to a helper file
    public final static int RESULT_LOAD_IMG = 12;

    @BindView(R.id.etSignupUsername)
    EditText etUsername;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etSignupPassword)
    EditText etPassword;

    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    @BindView(R.id.btnUploadProfileImage)
    Button btnUploadProfileImage;

    @BindView(R.id.btnSignUp)
    Button btnSignUp;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private File photoFile;

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

    @OnClick(R.id.btnUploadProfileImage)
    public void uploadProfileImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        startActivityForResult(intent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMG) {
            if (data != null) {
                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    photoFile = FileHelper.createReducedImageFile(this, bitmap);
                } catch (IOException ex) {
                    Log.d("OMG", "Error occurred while creating the file");
                }
            }
        }
    }

    @OnClick(R.id.btnSignUp)
    public void signUp() {
        final ParseUser user = new ParseUser();

        if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            Toast.makeText(this, "Passwords must match!", Toast.LENGTH_LONG).show();
        } else if(!FormatHelper.isEmailValid(etEmail.getText().toString())) {
            Toast.makeText(this, "Email must be valid!", Toast.LENGTH_LONG).show();
        } else {
            user.setUsername(etUsername.getText().toString());
            user.setPassword(etPassword.getText().toString());
            user.setEmail(etEmail.getText().toString());

            final ParseFile profileImage = new ParseFile(photoFile);

            profileImage.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if(e == null) {
                        user.put("profileImage", profileImage);
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(SignupActivity.this, "Signup was successful!",
                                            Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SignupActivity.this,
                                            HomeActivity.class));
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
            });
        }
    }
}
