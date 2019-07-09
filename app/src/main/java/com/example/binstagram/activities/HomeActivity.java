package com.example.binstagram.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.binstagram.R;
import com.example.binstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.btnCreatePost)
    ImageButton btnCreatePost;

    @BindView(R.id.btnRefresh)
    Button btnRefresh;

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
        startActivity(new Intent(this, SelectActivity.class));
    }

    @OnClick(R.id.btnLogOut)
    public void logOut() {
        ParseUser.logOut();
        Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.btnRefresh)
    public void refreshPosts() {
        loadTopPosts();
    }
}
