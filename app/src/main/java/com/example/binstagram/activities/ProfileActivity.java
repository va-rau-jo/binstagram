package com.example.binstagram.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.binstagram.R;
import com.example.binstagram.adapters.ProfilePostAdapter;
import com.example.binstagram.models.Post;
import com.example.binstagram.utils.FileHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ProfileActivity extends AppCompatActivity {

    // TODO: refactor to a helper file
    public final static int RESULT_LOAD_IMG = 12;

    @BindView(R.id.tvUsername)
    TextView tvUsername;

    @BindView(R.id.tvChangePicture)
    TextView tvChangePicture;

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.rvPosts)
    RecyclerView rvPosts;

    @BindView(R.id.tvPostCount)
    TextView tvPostCount;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ParseUser user;
    private ArrayList<Post> posts;
    private ProfilePostAdapter adapter;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setActionBar(toolbar);
        if(getActionBar() != null)
            getActionBar().setDisplayShowTitleEnabled(false);

        if(getIntent().getExtras() != null) {
            user = (ParseUser) getIntent().getExtras().get("user");
        }

        posts = new ArrayList<>();
        adapter = new ProfilePostAdapter(posts);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvPosts.setLayoutManager(gridLayoutManager);
        rvPosts.setAdapter(adapter);

        // Sets the bottom navigation bar on clicks
        addNavigationItemSelectedListener();

        // Add swiping up to refresh
        addSwipeRefreshListener();

        // Load the initial posts
        loadTopPosts();

        // Display views after loading everything but the post amount
        initializeView();
    }

    private void addNavigationItemSelectedListener() {

    }

    private void addSwipeRefreshListener() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                loadTopPosts();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void initializeView() {
        tvUsername.setText(user.getUsername());

        Log.i("testset", ParseUser.getCurrentUser().getObjectId());
        Log.i("testset", user.getObjectId());

        boolean sameUser = ParseUser.getCurrentUser().getObjectId().equals(user.getObjectId());

        tvChangePicture.setVisibility(sameUser ? View.VISIBLE : View.GONE);


        Glide.with(this)
                .load(user.getParseFile("profileImage").getUrl())
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivProfileImage);
    }

    @OnClick(R.id.tvChangePicture)
    public void changeProfilePictureOnClick() {
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
                    updateProfilePicture(FileHelper.createReducedImageFile(this, bitmap));
                } catch (IOException ex) {
                    Log.d("OMG", "Error occurred while creating the file");
                }
            }
        }
    }

    private void updateProfilePicture(final File profilePicture) {
        final ParseUser user = ParseUser.getCurrentUser();
        final ParseFile file = new ParseFile(profilePicture);

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    user.put("profileImage", file);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                reloadProfilePicture(profilePicture);

                                Toast.makeText(ProfileActivity.this, "Profile picture changed!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();

        postQuery.chronological()
                .withUser()
                .whereEqualTo("user", user);

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    for(int i = 0; i < objects.size(); i++) {
                        posts.add(objects.get(i));
                        adapter.notifyItemInserted(posts.size() - 1);
                    }
                    String postCount = posts.size() + "";
                    tvPostCount.setText(postCount);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void reloadProfilePicture(File profilePicture) {
        Glide.with(this)
                .load(profilePicture)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivProfileImage);
    }
}
