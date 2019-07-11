package com.example.binstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.binstagram.R;
import com.example.binstagram.activities.DetailActivity;
import com.example.binstagram.activities.ProfileActivity;
import com.example.binstagram.models.Post;
import com.parse.ParseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private ArrayList<Post> posts;
    private Context context;

    public PostAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    /**
     * Method called when the adapter has to display the data on a view, creates one view holder
     * @param viewGroup The parent view
     * @param viewType Type of the parent view, doesn't matter here
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
     * @param viewHolder The already created view holder that will be populated
     * @param index The index of the view holder
     */
    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder viewHolder, int index) {
        Post post = posts.get(index);
        ParseUser user = post.getUser();
        viewHolder.tvUsername.setText(user.getUsername());
        viewHolder.tvUsernameLabel.setText(user.getUsername());
        viewHolder.tvDescription.setText(post.getDescription());

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(viewHolder.ivImage);

        if(user.getParseFile("profileImage") != null) {
            Glide.with(context)
                    .load(user.getParseFile("profileImage").getUrl())
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(viewHolder.ivProfileImage);
        }
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

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("post", posts.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}