package me.blr3.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    // the base URL for loading images
    String imageBaseUrl;
    // the poster size to use when fetching images, part of the url
    String posterSize;

    public Config(JSONObject object) throws JSONException {
        // parse images object first which contains the poster size and imagebaseurl
        JSONObject images = object.getJSONObject("images");
        // get the image base url from json object and assign value to variable named imagebasedurl
        imageBaseUrl = images.getString("secure_base_url");
        // get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        // use the option at index 3 or w342 as a fallback
        posterSize = posterSizeOptions.optString(3, "w342");
    }

    // helper method for creating urls
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path); // concatenate all three
    }
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
