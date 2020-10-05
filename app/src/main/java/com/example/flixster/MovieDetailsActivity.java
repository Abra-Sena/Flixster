package com.example.flixster;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyD6pfRTB4Z66tJamvs2yDRS53G7MV24ms8";
    public static final String VIDEO_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    TextView tvTitle;
    TextView tvOverview;
    /*TextView tvFamily;
    TextView tvRelease;
    TextView tvLanguage;*/
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;
    Context context;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        ratingBar = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);

        /*tvRelease = findViewById(R.id.tvRelease);
        tvFamily = findViewById(R.id.tvFamily);
        tvLanguage = findViewById(R.id.tvLanguage);*/

        //retrieve data
        final Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float)movie.getRating());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEO_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
               try {
                   JSONArray results = json.jsonObject.getJSONArray("results");
                   if(results.length() == 0) {
                       //return image of the movie if no details found
                       //if phone is in landscape mode
                       if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                           imageUrl = movie.getBackdropPath();
                       } else { //phone is in portrait mode
                           imageUrl = movie.getPosterPath();
                       }
                       //return ;
                   }
                   String youtubeKey = results.getJSONObject(0).getString("key");
                   Log.d("MovieDetailsActivity", youtubeKey);
                   initializeYoutube(youtubeKey);
               } catch (JSONException e) {
                   Log.e("MovieDetailsActivity", "Failed to parse JSON", e);
               }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });

        /*tvRelease.setText(movie.getReleaseDate());
        tvFamily.setText(movie.getFamily());
        tvLanguage.setText(movie.getLanguage());*/
    }

    private void initializeYoutube(final String youtubeKey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.i("MovieDetailsActivity", "onInitializationSuccess");
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(youtubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.i("MovieDetailsActivity", "onInitializationFailure");
            }
        });
    }
}