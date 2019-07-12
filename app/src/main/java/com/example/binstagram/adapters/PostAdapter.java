package com.example.binstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.binstagram.R;
import com.example.binstagram.activities.CommentActivity;
import com.example.binstagram.activities.DetailActivity;
import com.example.binstagram.activities.ProfileActivity;
import com.example.binstagram.models.Comment;
import com.example.binstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<Post> posts;
    private Context context;

    public PostAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    /**
     * Method called when the adapter has to display the data on a view, creates one view holder
     *
     * @param viewGroup The parent view
     * @param viewType  Type of the parent view, doesn't matter here
     * @return The created view holder
     */
    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View tweetView = layoutInflater.inflate(R.layout.item_post, viewGroup, false);
        return new ViewHolder(tweetView);
    }

    /**
     * Method called to bind data to an already created view holder
     *
     * @param viewHolder The already created view holder that will be populated
     * @param index      The index of the view holder
     */
    @Override
    public void onBindViewHolder(@NonNull final PostAdapter.ViewHolder viewHolder, int index) {
        final Post post = posts.get(index);
        ParseUser user = post.getUser();
        viewHolder.tvUsername.setText(user.getUsername());
        viewHolder.tvUsernameLabel.setText(user.getUsername());
        viewHolder.tvDescription.setText(post.getDescription());

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(viewHolder.ivImage);

        if (user.getParseFile("profileImage") != null) {
            Glide.with(context)
                    .load(user.getParseFile("profileImage").getUrl())
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(viewHolder.ivProfileImage);
        }

        // Do like logic
        viewHolder.ibLike.setBackground(post.isNotLiked() ?
                ContextCompat.getDrawable(context, R.drawable.ufi_heart) :
                ContextCompat.getDrawable(context, R.drawable.ufi_heart_active));

        viewHolder.tvLikeCount.setText(String.format(Locale.ENGLISH, "%d", post.getLikes().length()));

        // Do all comment logic
        Comment.Query query = new Comment.Query();
        query.withUser();

        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if(e == null) {
                    JSONArray postComments = post.getComments();

                    for(int i = 0; i < postComments.length(); i++) {
                        TextView nameLabel = new TextView(context);
                        nameLabel.setTextColor(Color.BLACK);

                        TextView textView = new TextView(context);
                        try {
                            String objectId = postComments.getJSONObject(i).getString("objectId");

                            for (Comment comment : objects) {
                                if(comment.getObjectId().equals(objectId)) {
                                    //nameLabel.setText(comment.getUser().getUsername());
                                    String text = comment.getUser().getUsername() + " " + comment.getBody();
                                    textView.setText(text);
                                    break;
                                }
                            }
                            viewHolder.commentLayout.addView(textView);
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    /**
     * Helper method to clear the list of posts, so we can refresh
     */
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;

        @BindView(R.id.tvUsername)
        TextView tvUsername;

        @BindView(R.id.tvDescription)
        TextView tvDescription;

        // The label next to the description
        @BindView(R.id.tvUsernameLabel)
        TextView tvUsernameLabel;

        @BindView(R.id.ibLike)
        ImageButton ibLike;

        @BindView(R.id.tvLikeCount)
        TextView tvLikeCount;

        @BindView(R.id.commentLayout)
        LinearLayout commentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Go to the user's profile
            View.OnClickListener profileListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("user", posts.get(getAdapterPosition()).getUser());
                    context.startActivity(intent);
                }
            };

            tvUsername.setOnClickListener(profileListener);
            ivProfileImage.setOnClickListener(profileListener);
        }

        /**
         * Image onclick, goes to the detail view
         */
        @OnClick(R.id.ivImage)
        void viewDetails() {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("post", posts.get(getAdapterPosition()));
            context.startActivity(intent);
        }

        /**
         * Comment button onclick, goes to comment activity
         */
        @OnClick(R.id.ibComment)
        void comment() {
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("post", posts.get(getAdapterPosition()));
            context.startActivity(intent);
        }

        /**
         * Like button onclick, likes and unlikes
         */
        @OnClick(R.id.ibLike)
        void likePost() {
            final Post post = posts.get(getAdapterPosition());

            if (post.isNotLiked()) {
                ibLike.setBackground(ContextCompat.getDrawable(context, R.drawable.ufi_heart_active));
                post.likePost(ParseUser.getCurrentUser());
            } else {
                ibLike.setBackground(ContextCompat.getDrawable(context, R.drawable.ufi_heart));
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
    }
}