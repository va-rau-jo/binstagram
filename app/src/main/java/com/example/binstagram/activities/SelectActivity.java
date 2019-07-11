package com.example.binstagram.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    @BindView(R.id.btnPhoto)
    ImageButton btnPhoto;

    @BindView(R.id.btnUploadImage)
    ImageButton btnUploadImage;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    File photoFile;

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

    @OnClick(R.id.btnUploadImage)
    public void uploadImage() {
        FileHelper.getImageFromGallery(this);
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

            //Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileProvider);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap selectedImage = null;
            if (requestCode == FileHelper.RESULT_LOAD_IMG) {
                if (data != null) {
                    try {
                        selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                data.getData());

                    } catch (IOException ex) {
                        Log.d("OMG", "Error occurred while creating the file");
                    }
                }
            } else if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                selectedImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            }

            File resizedFile = FileHelper.createReducedImageFile(this, selectedImage);
            Intent intent = new Intent(this, CaptionActivity.class);
            intent.putExtra("image", resizedFile);
            startActivity(intent);
            finish();
        }
    }
}
