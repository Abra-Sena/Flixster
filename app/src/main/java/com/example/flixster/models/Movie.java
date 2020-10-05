package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel //annotation that indicates the class is parcelable
public class Movie {
    //must be public for Parceler
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    /*String language;
    String releaseDate;
    String family;*/
    double rating;
    int movieId;

    //constructor is empty because required for Parceler
    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        rating = jsonObject.getDouble("vote_average");
        movieId = jsonObject.getInt("id");

        /*language = jsonObject.getString("original_language");
        releaseDate = jsonObject.getString("release_date");
        family = jsonObject.getString("adult");*/
    }

    public static List<Movie> fromJSONArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return  movies;
    }

    public String getPosterPath() {
        // make poster_path visible by adding the url at 'format' in the return value (this is hard code)
        // the relative url '6bCplVkhowCjTHXWv49UjRPn0eK.jpg' is replaced by %s
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    /*public String getLanguage() {
        return language;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getFamily() {
        return family;
    }*/

    public double getRating() {
        return rating;
    }

    public int getMovieId() {
        return movieId;
    }
}
