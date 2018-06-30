package me.blr3.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.blr3.flicks.models.Config;
import me.blr3.flicks.models.Movie;

public class MovieListActivity extends AppCompatActivity {

    //  constants for string values we want to use over and over
    // the base URL for the API
    // final variable cannot be modified
    // first part of the url call it will always be the same
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // the parameter name for the API key
    public final static String API_KEY_PRAM = "api_key";
    // the API key -- TODO move to a secure location
    // tag for logging from this activity ( logging control how errors are handled and shown to user
    public final static String TAG = "MovieListActivity";

    // instance fields
    AsyncHttpClient client;
    // the List of currently playing movies
    ArrayList<Movie> movies;
    // the recycler view
    RecyclerView rvMovies;
    // the adapter wired to the recycler view
    MovieAdapter adapter;
    // image config
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        // initialize the client
        client = new AsyncHttpClient();
        // initialize the list of movies
        movies = new ArrayList<>();
        // initialize the adapter -- movies array cannot be reinitialized after this point
        adapter = new MovieAdapter(movies);

        // resolve the recycler view and connect a layout manager and the adapter
        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        // get the configuration on app creation
        getConfiguration();


    }

    // get the list of currently playing movies from the API
    private void getNowPlaying() {
        // create the URL
        String url = API_BASE_URL + "/movie/now_playing";
        // set the request parameters - parameters that get appended to the URL
        RequestParams params = new RequestParams();
        // set API Key value
        // (key,value)
        params.put(API_KEY_PRAM, getString(R.string.api_key)); // API key, always required
        // execute a GET request expecting a JSON object response because it begins with curly braces
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                // load the results into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    // iterate through result set and create Movie object
                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        // notify adapter that a row was added
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    // log indicates operation completed successfully -> use log i method i for information
                    // %s string place holder gets replaced by results length through string.format
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                    // get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                logError("Failed to get data from now_playing endpoint", throwable, true);
            }
        });
    }

    // get the configuration from the API
    private void getConfiguration() {
        // create the URL
        String url = API_BASE_URL + "/configuration";
        // set the request parameters - parameters that get appended to the URL
        RequestParams params = new RequestParams();
        // set API Key value
        // (key,value)
        params.put(API_KEY_PRAM, getString(R.string.api_key)); // API key, always required
        // execute a GET request expecting a JSON object response because it begins with curly braces
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // super.onSuccess(statusCode, headers, response);
                try {
                    config = new Config(response);


                    Log.i(TAG,
                            String.format("Loaded configuration with imageBaseUrl %s and posterSize %s",
                                    config.getImageBaseUrl(),
                                    config.getPosterSize()));
                    // pass config to adapter
                    adapter.setConfig(config);
                    // get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                } // initializes a new instance of the jsonexception class with a specified error message and a reference to the inner exception that is the cause of this exception
                // get string retrieves value of indicated i this case secure_base_url
                    }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
               // super.onFailure(statusCode, headers, throwable, errorResponse);
                logError("Failed getting configuration", throwable, true);
                    } // throwable = log a stack trace for the error

    });

    }

    // handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
    // string message = where the problem happened / throwable = base class of all errors in java/ alertuser = lets us decide if we want the error displayed to the user
    // always log the error
        // log.e logs information about errors
        Log.e(TAG, message, error);
        // alert the user to avoid silent errors
        if (alertUser) {
          // toast = way of letting a user know an operation completed or something went wrong
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            // maketext = contains a text view with the text from a resource
            // return message and show the message for a long time and show the view for the specified duration
        }

    }
}
