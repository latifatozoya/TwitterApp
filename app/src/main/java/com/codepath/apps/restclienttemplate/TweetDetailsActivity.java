package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.target.Target;
import com.codepath.apps.restclienttemplate.models.GlideApp;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetailsActivity extends AppCompatActivity {

    //the tweet to display

    TwitterClient client;
    Tweet tweet;
    Button bt2, btn;
    TextView tvName;
    TextView tvUserName;
    TextView tvBody;
    ImageView ivProfileImage;
    private final int REQUEST_CODE = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tvBody = (TextView) findViewById(R.id.tvBody);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        bt2 = (Button) findViewById(R.id.bt2);
        btn = (Button) findViewById(R.id.bt4);
        client= new TwitterClient(getApplicationContext());
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d("TweetDetailsActivity", String.format("Showing details for '%s'", tweet.getClass()));

        // set the title and overview
        bt2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), replyActivity.class);
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                //getApplicationContext().startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                client.favoriteTweet(tweet.uid, new JsonHttpResponseHandler() {

                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Tweet tweet = Tweet.fromJSON(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        v.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_vector_heart));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        throwable.printStackTrace();
                    }
                });

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        Log.d("Tweet", "entered OnActivity");
        if (resultCode == RESULT_OK && (requestCode == REQUEST_CODE || requestCode == 10)) {
            // Extract name value from result extras
            Intent intent = new Intent(TweetDetailsActivity.this, TimelineActivity.class);
            intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
            startActivityForResult(intent, REQUEST_CODE);

        }
    }
}
