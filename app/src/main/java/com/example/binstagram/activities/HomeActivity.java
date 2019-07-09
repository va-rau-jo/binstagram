package com.example.binstagram.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.binstagram.R;
import com.example.binstagram.adapters.PostAdapter;
import com.example.binstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<Post> posts;
    private PostAdapter adapter;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.rvPosts)
    RecyclerView rvPosts;

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

        posts = new ArrayList<>();
        adapter = new PostAdapter(HomeActivity.this, posts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPosts.setLayoutManager(linearLayoutManager);
        rvPosts.setAdapter(adapter);

        // Add swiping up to refresh
        addSwipeRefreshListener();

        // Load the initial posts
        loadTopPosts();
    }

    private void addSwipeRefreshListener() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Post.Query postQuery = new Post.Query();
                postQuery.getTop().withUser();

                postQuery.findInBackground(new FindCallback<Post>() {
                    @Override
                    public void done(List<Post> objects, ParseException e) {
                        if(e == null) {
                            adapter.clear();
                            for(int i = objects.size() - 1; i >= 0; i--) {
                                posts.add(objects.get(i));
                                adapter.notifyItemInserted(posts.size() - 1);
                                swipeContainer.setRefreshing(false);
                            }
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    for(int i = objects.size() - 1; i >= 0; i--) {
                        posts.add(objects.get(i));
                        adapter.notifyItemInserted(posts.size() - 1);
                    }
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
