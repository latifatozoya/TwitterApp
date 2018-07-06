package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.target.Target;
import com.codepath.apps.restclienttemplate.models.GlideApp;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetailsActivity extends AppCompatActivity {

    //the tweet to display

    Tweet tweet;
    Button bt2;
    TextView tvName;
    TextView tvUserName;
    TextView tvBody;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tvBody = (TextView) findViewById(R.id.tvBody);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        bt2 = (Button) findViewById(R.id.bt2);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d("TweetDetailsActivity", String.format("Showing details for '%s'", tweet.getClass()));

        // set the title and overview
        bt2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), replyActivity.class);
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                getApplicationContext().startActivity(intent);
            }
        });
        tvBody.setText(tweet.getbody());
        tvName.setText(tweet.getuser().name);
        tvUserName.setText(tweet.getuser().screenName);
        int radius = 30;
        int margin = 0;
        GlideApp.with(this)
                .load(tweet.user.profileImageUrl)
                .override(100, Target.SIZE_ORIGINAL)
                .fitCenter()
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(ivProfileImage);
    }
}
