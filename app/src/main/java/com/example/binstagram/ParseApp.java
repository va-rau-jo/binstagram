package com.example.binstagram;

import android.app.Application;

import com.example.binstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("varaujo-app-id")
                .clientKey("Googlesucksomegalul")
                .server("http://varaujo-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
