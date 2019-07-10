package com.example.binstagram.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.binstagram.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileHelper {

    private final static String PHOTO_FILE_NAME = "photo.jpg";

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
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

    public static File createReducedImageFile(Context context, File file) {
        Bitmap rawTakenImage = BitmapFactory.decodeFile(file.getPath());
        Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 512);

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
