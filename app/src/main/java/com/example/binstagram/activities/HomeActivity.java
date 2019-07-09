package com.example.binstagram.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.binstagram.R;
import com.example.binstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    public static final int RESULT_LOAD_IMG = 12;

    String currentFilePath = "";

    @BindView(R.id.btnCreatePost)
    Button btnCreatePost;

    @BindView(R.id.btnRefresh)
    Button btnRefresh;

    @BindView(R.id.etDescription)
    EditText etDescription;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btnLogOut)
    Button btnLogOut;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setActionBar(toolbar);

        if(getActionBar() != null)
            getActionBar().setDisplayShowTitleEnabled(false);

        loadTopPosts();
    }

    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    for(int i = 0; i < objects.size(); i++)
                        Log.d("HomeAcitivyt", objects.get(i).getDescription());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.btnCreatePost)
    public void getFileFromPhone() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    @OnClick(R.id.btnLogOut)
    public void logOut() {
        ParseUser.logOut();
        Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_LOAD_IMG) {
            if (data != null) {
                try {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.d("OMG", "Error occurred while creating the file");
                    }

                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(photoFile);
                    // Copying
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    createPost();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
        currentFilePath = image.getAbsolutePath();
        return image;
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void createPost() {
        Post newPost = new Post();
        newPost.setDescription(etDescription.getText().toString());
        newPost.setImage(new ParseFile(new File(currentFilePath)));
        newPost.setUser(ParseUser.getCurrentUser());

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Log.d("asfasdf", "we gucci");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick(R.id.btnRefresh)
    public void refreshPosts() {
        loadTopPosts();
    }
}
