package com.example.binstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final Integer QUERY_LIMIT = 3;
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_COMMENTS = "user";

    public JSONArray userLikes() {
        return getJSONArray("likedBy");
    }

    public void likePost(ParseUser user) {
        add("likedBy", user);
    }

    public void unlikePost(ParseUser user) {
        List<ParseUser> users = new ArrayList<>();
        users.add(user);
        removeAll("likedBy", users);
    }

    public boolean isNotLiked() {
        JSONArray likes = userLikes();

        if(likes != null) {
            for (int i = 0; i < likes.length(); i++) {
                try {
                    if (likes.getJSONObject(i).getString("objectId")
                            .equals(ParseUser.getCurrentUser().getObjectId()))
                        return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public JSONArray getComments() {
        return getJSONArray(KEY_COMMENTS);
    }

    public Date getTimestamp() { return getCreatedAt(); }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public void addComment(Comment comment) {
        add(KEY_COMMENTS, comment);
    }

    public static class Query extends ParseQuery {
        public Query() {
            super(Post.class);
        }

        public Query chronological() {
            orderByDescending("createdAt");
            return this;
        }

        public Query getNext(int alreadyLoadedAmount) {
            setSkip(alreadyLoadedAmount);
            setLimit(QUERY_LIMIT);
            return this;
        }

        public Query getTop() {
            setLimit(QUERY_LIMIT);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }
    }
}
