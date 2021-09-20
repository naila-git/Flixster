package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    //save as a constant
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    // we will add a tag - want this so we can easily log data
    public static final String TAG = "MainActivity";

    List<Movie> movies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        //create the adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        //set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);
        //set a layout manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        AsyncHttpClient client = new AsyncHttpClient();
        // going to make a get request on the url by NOW_PLAYING_URL and then pass in a callback
        // we want to use a json response handler because the movie api database returns json
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
               Log.d(TAG, "onSuccess");
                // the data we want is in the json object
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    // we want to take our json array and turn it into a list of movies (in Movie.java)
                    Log.i(TAG, "Results:" + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies:" + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG,"Hit json exception",e);
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
                // log statements can be used to debug a program or you can debug a program
                // by adding a break point




            }
        });
        // ); this symbolization completes the statement
    }
}