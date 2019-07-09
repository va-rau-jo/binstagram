package com.example.binstagram.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.binstagram.utils.BitmapScaler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectActivity extends AppCompatActivity {

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

    public static final int RESULT_LOAD_IMG = 12;

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
        if(getActionBar() != null)
            getActionBar().setDisplayShowTitleEnabled(false);
    }


    public String photoFileName = "photo.jpg";
    File photoFile;

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
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.binstagram.fileprovider", photoFile);
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
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.d("OMG", "Error occurred while creating the file");
                    }
                }
            } else if(requestCode != CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
                Toast.makeText(this, "Request code did not match", Toast.LENGTH_SHORT).show();
            }

            File resizedFile = createReducedImageFile(photoFile);
            Intent intent = new Intent(this, CaptionActivity.class);
            intent.putExtra("image", resizedFile);
            startActivity(intent);
        }
    }

    private File createReducedImageFile(File photoFile) {
        Bitmap rawTakenImage = BitmapFactory.decodeFile(photoFile.getPath());
        Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 256);

        // Configure byte output stream
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        try {
            File resizedFile = getPhotoFileUri(photoFileName + "_resized");
            resizedFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(resizedFile);

            fos.write(bytes.toByteArray());
            fos.close();

            return resizedFile;
        } catch (IOException e) {
            Toast.makeText(this, "Error resizing the image given", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //TODO: Refactor
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

        // Returns the File for a photo stored on disk given the fileName
        public File getPhotoFileUri (String fileName) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), getString(R.string.app_name));

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d("SelectActivity", "failed to create directory");
            }

            // Return the file target for the photo based on filename
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            return file;
        }
}
