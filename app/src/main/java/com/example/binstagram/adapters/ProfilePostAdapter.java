package com.example.binstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.binstagram.R;
import com.example.binstagram.activities.DetailActivity;
import com.example.binstagram.models.Post;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder>{

    private ArrayList<Post> posts;
    private Context context;

    public ProfilePostAdapter(ArrayList<Post> posts) {
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
    public ProfilePostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View tweetView = layoutInflater.inflate(R.layout.item_profile_post, viewGroup, false);
        return new ViewHolder(tweetView);
    }

    /**
     * Method called to bind data to an already created view holder
     * @param viewHolder The already created view holder that will be populated
     * @param index The index of the view holder
     */
    @Override
    public void onBindViewHolder(@NonNull ProfilePostAdapter.ViewHolder viewHolder, int index) {
        Post post = posts.get(index);

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(viewHolder.ivImage);
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