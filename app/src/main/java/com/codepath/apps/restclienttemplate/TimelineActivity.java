package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                launchComposeView();
                return true;

            case R.id.miProfile:
                //showProfileView();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String name = data.getExtras().getString("name");
            int code = data.getExtras().getInt("code", 0);
            // Toast the name to display temporarily on screen
            Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);

        }
    }

    public void launchComposeView() {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("username", "foobar");
        i.putExtra("in_reply_to", "george");
        i.putExtra("code", 400);
        startActivity(i); // brings up the second activity
    }

    public void onSubmit(View v) {
        // closes the activity and returns to first screen

        EditText etName = (EditText) findViewById(R.id.et_simple);
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("name", etName.getText().toString());
        data.putExtra("code", 200); // ints work too
        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        this.finish();
    }

    private final int REQUEST_CODE = 20;
    public void onClick(View view) {
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("mode", 2); // pass arbitrary data to launched activity
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApp.getRestClient(getApplicationContext());
        //find the recyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        //init the array list (data source)
        tweets = new ArrayList<>();
        //construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets);
        //RecyclerView setup
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);
        populateTimeline();

        String username = getIntent().getStringExtra("username");
        String inReplyTo = getIntent().getStringExtra("in_reply_to");
        int code = getIntent().getIntExtra("code", 0);

    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("TwitterClient", response.toString());

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                            tweets.add(tweet);
                            tweetAdapter.notifyItemInserted(tweets.size() - 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
         });
    }
}
