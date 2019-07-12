package com.example.binstagram.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toolbar;

import com.example.binstagram.R;
import com.example.binstagram.models.Comment;
import com.example.binstagram.models.Post;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentActivity extends AppCompatActivity {

    @BindView(R.id.etComment)
    EditText etComment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Post post;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);

        setActionBar(toolbar);
        if(getActionBar() != null)
            getActionBar().setDisplayShowTitleEnabled(false);

        if(getIntent().getExtras() != null) {
            post = getIntent().getExtras().getParcelable("post");
        }
    }

    @OnClick(R.id.btnPostComment)
    public void postComment() {
        ParseACL parseACL = new ParseACL(ParseUser.getCurrentUser());
        parseACL.setPublicReadAccess(true);

        ParseUser.getCurrentUser().setACL(parseACL);

        Comment comment = new Comment();
        comment.setUser(ParseUser.getCurrentUser());
        comment.setBody(etComment.getText().toString());
        comment.setACL(new ParseACL(ParseUser.getCurrentUser()));

        post.addComment(comment);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    startActivity(new Intent(CommentActivity.this, HomeActivity.class));
                    finish();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
