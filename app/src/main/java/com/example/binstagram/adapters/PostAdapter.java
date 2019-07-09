package com.example.binstagram.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.binstagram.R;
import com.example.binstagram.activities.DetailActivity;
import com.example.binstagram.models.Post;
import com.parse.ParseImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private ArrayList<Post> posts;
    private Context context;
    private Activity activity;

    public PostAdapter(Activity context, ArrayList<Post> posts) {
        this.posts = posts;
        this.activity = context;
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
        viewHolder.tvUsername.setText(post.getUser().getUsername());
        viewHolder.tvUsernameLabel.setText(post.getUser().getUsername());
        viewHolder.tvDescription.setText(post.getDescription());

        viewHolder.ivImage.setParseFile(post.getImage());
        viewHolder.ivImage.loadInBackground();
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
        ParseImageView ivImage;

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

            itemView.setOnClickListener(new View.OnClickListener() {
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