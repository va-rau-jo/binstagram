package com.example.binstagram.utils;

import android.support.v4.widget.SwipeRefreshLayout;

import com.example.binstagram.adapters.PostAdapter;
import com.example.binstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class FeedHelper {

    /**
     * Loads the top posts by querying the parse server with the top x amount of posts from
     * the current user
     * @param posts List of posts
     * @param adapter PostAdapter that connects to the recyclerview in that activity
     * @param swipeContainer Optional swipecontainer for further loading
     */
    public static void loadTopPosts(final ArrayList<Post> posts, final PostAdapter adapter,
                              final SwipeRefreshLayout swipeContainer, final Post.Query postQuery) {

    }

    /**
     * Loads the next posts for the infinite scrolling funtionality
     */

}
