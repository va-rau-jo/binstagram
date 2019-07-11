package com.example.binstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Post")
public class Comment extends ParseObject {

    public static final Integer QUERY_LIMIT = 3;
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
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

    public static class Query extends ParseQuery {
        public Query() {
            super(Comment.class);
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
