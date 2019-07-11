package com.example.binstagram.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.example.binstagram.R;
import com.example.binstagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaptionActivity extends AppCompatActivity {

    @BindView(R.id.ivImagePreview)
    ImageView ivImagePreview;

    @BindView(R.id.etConfirmDescription)
    EditText etDescription;

    @BindView(R.id.btnConfirmPost)
    Button btnConfirmPost;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private File imageFile;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.INVISIBLE);

        setActionBar(toolbar);
        if(getActionBar() != null)
            getActionBar().setDisplayShowTitleEnabled(false);

        if(getIntent().getExtras() != null) {
            imageFile = (File) getIntent().getExtras().get("image");
            setPreviewImage();
        }
    }

    private void setPreviewImage() {
        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        ivImagePreview.setImageBitmap(myBitmap);
    }

    @OnClick(R.id.btnConfirmPost)
    public void confirmPost() {
        progressBar.setVisibility(View.VISIBLE);
        createPost(etDescription.getText().toString(), new ParseFile(imageFile),
                ParseUser.getCurrentUser(), progressBar);
    }

    private void createPost(String description, ParseFile image, ParseUser user,
                            final ProgressBar progressBar) {
        Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(image);
        newPost.setUser(user);
        newPost.put("comments", new JSONArray());
        newPost.put("likedBy", new JSONArray());

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(CaptionActivity.this, HomeActivity.class));
                    finish();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
