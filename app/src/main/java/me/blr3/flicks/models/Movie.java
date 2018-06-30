package me.blr3.flicks.models;

// want to track all of the necessary information for the movie that you want to display

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {

    // instance variables - values from API
    // private -> can only be accessed from inside the movie class
    private String title;
    private String overview;
    private String posterPath; // only the path not the full url

    // add constructor (create an object of movie class) that we can initialize from JSON data
    // constructors look like a method declaration
    // still a little confused about what we are passing through as arguments
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");

    }
// getters so we can access the private instance variables outside the class
    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
