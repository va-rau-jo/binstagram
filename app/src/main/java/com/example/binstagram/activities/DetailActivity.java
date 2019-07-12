package com.example.binstagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.binstagram.R;
import com.example.binstagram.models.Post;
import com.example.binstagram.utils.FormatHelper;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.ivImage)
    ImageView ivImage;

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;

    @BindView(R.id.tvUsername)
    TextView tvUsername;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.tvUsernameLabel)
    TextView tvUsernameLabel;

    @BindView(R.id.tvTimestamp)
    TextView tvTimestamp;

    @BindView(R.id.ibLike)
    ImageButton ibLike;

    @BindView(R.id.tvLikeCount)
    TextView tvLikeCount;

    @BindView(R.id.ibComment)
    ImageButton ibComment;

    @BindView(R.id.tvCommentCount)
    TextView tvCommentCount;

    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if(getIntent().getExtras() != null) {
            post = getIntent().getExtras().getParcelable("post");
        } else {
            Toast.makeText(this, "Could not load post", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        initializeView();
    }

    @OnClick(R.id.ibLike)
    public void likePost() {
        if (post.isNotLiked()) {
            ibLike.setBackground(ContextCompat.getDrawable(this, R.drawable.ufi_heart_active));
            post.likePost(ParseUser.getCurrentUser());
        } else {
            ibLike.setBackground(ContextCompat.getDrawable(this, R.drawable.ufi_heart));
            post.unlikePost(ParseUser.getCurrentUser());
        }

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    tvLikeCount.setText(String.format(Locale.ENGLISH, "%d", post.getLikes().length()));
                }
            }
        });
    }


    private void initializeView() {
        try {
            // Random error not find the username
            tvUsername.setText(post.getUser().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvTimestamp.setText(FormatHelper.getRelativeTimeAgo(post.getTimestamp()));

        tvUsernameLabel.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());

        ibLike.setBackground(post.isNotLiked() ?
                ContextCompat.getDrawable(this, R.drawable.ufi_heart) :
                ContextCompat.getDrawable(this, R.drawable.ufi_heart_active));

        tvLikeCount.setText(String.format(Locale.ENGLISH, "%d", post.getLikes().length()));

        Glide.with(this)
                .load(post.getUser().getParseFile("profileImage").getUrl())
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivProfileImage);

        Glide.with(this)
                .load(post.getImage().getUrl())
                .into(ivImage);
    }
}