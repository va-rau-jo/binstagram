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
import com.example.binstagram.models.EndlessRecyclerViewScrollListener;
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

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.rvPosts)
    RecyclerView rvPosts;

    @BindView(R.id.btnCreatePost)
    ImageButton btnCreatePost;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btnLogOut)
    Button btnLogOut;

    private ArrayList<Post> posts;
    private PostAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;

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

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextData();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);

        // Add swiping up to refresh
        addSwipeRefreshListener();

        // Load the initial posts
        loadTopPosts();
    }

    /**
     * Loads the next posts for the infinite scrolling funtionality
     */
    private void loadNextData() {
        final Post.Query postQuery = new Post.Query();
        postQuery
                .chronological()
                .withUser()
                .getNext(posts.size());

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    for(int i = 0; i < objects.size(); i++) {
                        posts.add(objects.get(i));
                        adapter.notifyItemInserted(posts.size() - 1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Adds the OnRefreshListener to the SwipeRefreshLayout, so posts are reloaded
     */
    private void addSwipeRefreshListener() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                adapter.clear();
                loadTopPosts();
            }
        });
    }

    /**
     * Loads the top posts by querying the parse server with the top x amount of posts from
     * the current user
     */
    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery
                .chronological()
                .withUser()
                .getTop();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    for(int i = 0; i < objects.size(); i++) {
                        posts.add(objects.get(i));
                        adapter.notifyItemInserted(posts.size() - 1);
                    }
                } else {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }

    /**
     * Navigates to the SelectActivity class to open the camera or select an image from the gallery
     */
    @OnClick(R.id.btnCreatePost)
    public void getFileFromPhone() {
        startActivity(new Intent(this, SelectActivity.class));
    }

    /**
     * Logs the user out and sends them back to the LoginActivity
     */
    @OnClick(R.id.btnLogOut)
    public void logOut() {
        ParseUser.logOut();
        Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
