package com.example.binstagram.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.binstagram.R;
import com.example.binstagram.utils.FileHelper;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectActivity extends AppCompatActivity {

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

    public final static int RESULT_LOAD_IMG = 12;

    @BindView(R.id.btnPhoto)
    ImageButton btnPhoto;

    @BindView(R.id.btnUploadImage)
    ImageButton btnUploadImage;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);

        setActionBar(toolbar);
        if (getActionBar() != null)
            getActionBar().setDisplayShowTitleEnabled(false);
    }

    private File photoFile;

    @OnClick(R.id.btnUploadImage)
    public void uploadImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    @OnClick(R.id.btnPhoto)
    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = FileHelper.getPhotoFileUri(this);

        // wrap File object into a content provider, required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, getString(R.string.file_provider), photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            // Taking a photo
            if (requestCode == RESULT_LOAD_IMG) {
                if (data != null) {
                    try {
                        photoFile = FileHelper.createImageFile(this);
                    } catch (IOException ex) {
                        Log.d("OMG", "Error occurred while creating the file");
                    }
                }
            } else if (requestCode != CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                Toast.makeText(this, "Request code did not match", Toast.LENGTH_SHORT).show();
            }

            File resizedFile = FileHelper.createReducedImageFile(this, photoFile);
            Intent intent = new Intent(this, CaptionActivity.class);
            intent.putExtra("image", resizedFile);
            startActivity(intent);
            finish();
        }
    }
}
