package com.example.binstagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.binstagram.R;
import com.example.binstagram.models.Post;
import com.example.binstagram.utils.FormatHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class DetailActivity extends AppCompatActivity {
    private Post post;

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

    private void initializeView() {
        tvUsername.setText(post.getUser().getUsername());

        tvTimestamp.setText(FormatHelper.getRelativeTimeAgo(post.getTimestamp()));

        tvUsernameLabel.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());

        Glide.with(this)
                .load(post.getUser().getParseFile("profileImage").getUrl())
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivProfileImage);

        Glide.with(this)
                .load(post.getImage().getUrl())
                .into(ivImage);
    }
}