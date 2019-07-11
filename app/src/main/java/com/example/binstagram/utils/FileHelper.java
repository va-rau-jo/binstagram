package com.example.binstagram.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.binstagram.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    private final static String PHOTO_FILE_NAME = "photo.jpg";

    public final static int RESULT_LOAD_IMG = 1046;

    public static void getImageFromGallery(Activity context) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            // Bring up gallery to select a photo
            context.startActivityForResult(intent, RESULT_LOAD_IMG);
        }
    }

    public static File createReducedImageFile(Context context, Bitmap rawImage) {
        Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawImage, 512);

        // Configure byte output stream
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        try {
            File resizedFile = getPhotoFileUri(context);
            resizedFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(resizedFile);

            fos.write(bytes.toByteArray());
            fos.close();

            return resizedFile;
        } catch (IOException e) {
            Toast.makeText(context, "Error resizing the image given", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public static File getPhotoFileUri(Context context) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                context.getString(R.string.app_name));

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("SelectActivity", "failed to create directory");
        }
        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + PHOTO_FILE_NAME + "_resized");
    }

}
