package com.example.binstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    private static final String KEY_BODY = "body";
    private static final String KEY_USER = "user";
    private static final String KEY_RESPONDING_TO = "respondingTo";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public String getBody() {
        return getString(KEY_BODY);
    }

    public ParseUser getRespondingTo() {
        return getParseUser(KEY_RESPONDING_TO);
    }

    public Date getTimestamp() { return getCreatedAt(); }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public void setBody(String body) {
        put(KEY_BODY, body);
    }

    public void setRespondingTo(ParseUser respondingTo) {
        put(KEY_RESPONDING_TO, respondingTo);
    }

    public static class Query extends ParseQuery {
        public Query() {
            super(Comment.class);
        }

        public Comment.Query withUser() {
            include(KEY_USER);
            return this;
        }
    }
}
