package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class replyActivity extends AppCompatActivity {

    Tweet tweet;
    TwitterClient client;
    EditText simpleEditText;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        simpleEditText = (EditText) findViewById(R.id.et_simple);
        String strValue = simpleEditText.getText().toString();
        client = TwitterApp.getRestClient(this);

        EditText etSimple = findViewById(R.id.et_simple);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d("replyActivity", String.format("Showing details for '%s'", tweet.getClass()));
        id = tweet.uid;
        etSimple.setText(tweet.user.screenName);
    }

    public void onSuccess(View view) {
        client.replyTweet(simpleEditText.getText().toString(), id, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    Intent data = new Intent();
                    data.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK, data);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
            }
        });
    }


}