package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codepath.apps.restclienttemplate.models.GlideApp;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    Button bt1;
    public TweetAdapter item;

    public void clear() {
        item.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        item.addAll(list);
        notifyDataSetChanged();
    }

    //pass in the tweets array in the constructor
    List<Tweet> mTweets;
    Context context;

    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }
    //for each row, inflate the layout and cache references into ViewHolder


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;

    }

    //bind the values based on the position of the element

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //get data according to position
        Tweet tweet = mTweets.get(position);

        //populate the view according to this data
        holder.tvUsername.setText(tweet.user.screenName);
        holder.tvName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvDate.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.createdAt));
        holder.progressBar.setVisibility(View.VISIBLE);
        int radius = 30;
        int margin = 0;

        GlideApp.with(context)
                .load(tweet.user.profileImageUrl)
                .override(100, Target.SIZE_ORIGINAL)
                .fitCenter() // scale to fit entire image within ImageView
                .transform(new RoundedCornersTransformation(radius, margin))

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public void addAll() {
    }
    //create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvName;
        public TextView tvBody;
        public TextView tvDate;
        public ProgressBar progressBar;


        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            bt1 = (Button) itemView.findViewById(R.id.bt2);
            progressBar = itemView.findViewById(R.id.progressBar);
            Log.d("pb", ""+R.id.progressBar);
            itemView.setOnClickListener(this);
            bt1.setOnClickListener(this);
        }

       public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                if(v.getId() == R.id.bt2) {
                    Tweet tweet = mTweets.get(position);
                    Intent intent = new Intent(context, replyActivity.class);
                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    context.startActivity(intent);
                }
                else {
                    // get the movie at the position, this won't work if the class is static
                    Tweet tweet = mTweets.get(position);
                    // create intent for the new activity
                    Intent intent = new Intent(context, TweetDetailsActivity.class);
                    // serialize the movie using parceler, use its short name as a key
                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    // show the activity
                    context.startActivity(intent);
                }
            }
        }
    }
}
