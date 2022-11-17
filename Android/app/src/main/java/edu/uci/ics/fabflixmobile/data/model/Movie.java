package edu.uci.ics.fabflixmobile.data.model;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Movie class that captures movie information for movies retrieved from MovieListActivity
 */
public class Movie {
    private final String id;
    private final String name;
    private final short year;
    private final String director;
    private final double rating;
    private final ArrayList<String> genres;
    private final ArrayList<String> stars;

    public Movie(String id, String name, short year, String director, double rating) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.director = director;
        this.rating = rating;
        genres = new ArrayList<>();
        stars = new ArrayList<>();
    }

    public Movie(JSONObject movieJson) throws JSONException
    {
        id = movieJson.getString("movie_id");
        name = movieJson.getString("movie_title");
        year = (short) movieJson.getInt("movie_year");
        director = movieJson.getString("movie_director");
        rating = movieJson.getDouble("movie_rating");
        genres = new ArrayList<>();
        addGenres(movieJson);
        stars = new ArrayList<>();
        addStars(movieJson);
    }

    public Movie(JSONObject movieJson, int max_genres, int max_stars) throws JSONException
    {
        id = movieJson.getString("movie_id");
        name = movieJson.getString("movie_title");
        year = (short) movieJson.getInt("movie_year");
        director = movieJson.getString("movie_director");
        rating = movieJson.getDouble("movie_rating");
        genres = new ArrayList<>();
        addGenres(movieJson, max_genres);
        stars = new ArrayList<>();
        addStars(movieJson, max_stars);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public short getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public double getRating() {
        return rating;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<String> getStars() {
        return stars;
    }

    public String getGenresString()
    {
        if(genres.size() <= 0)  return "";

        StringBuilder genresStr = new StringBuilder(genres.get(0));
        for(int i = 1; i < genres.size(); ++i)
        {
            genresStr.append(", ").append(genres.get(i));
        }
        return genresStr.toString();
    }

    public String getStarsString()
    {
        if(stars.size() <= 0)   return "";

        StringBuilder starsStr = new StringBuilder(stars.get(0));
        for(int i = 1; i < stars.size(); ++i)
        {
            starsStr.append(", ").append(stars.get(i));
        }
        return starsStr.toString();
    }

    public void addGenre(String genre)
    {
        genres.add(genre);
    }

    public void addStar(String star)
    {
        stars.add(star);
    }

    public void addStars(JSONObject movieJson) throws JSONException {
        JSONArray stars = movieJson.getJSONArray("movie_stars");
        for(int i = 0; i < stars.length(); ++i)
        {
            JSONObject star = (JSONObject) stars.get(i);
            addStar(star.getString("star_name"));
        }
    }

    public void addStars(JSONObject movieJson, int max_stars) throws JSONException {
        JSONArray stars = movieJson.getJSONArray("movie_stars");
        for(int i = 0; i < stars.length() && i < max_stars; ++i)
        {
            JSONObject star = (JSONObject) stars.get(i);
            addStar(star.getString("star_name"));
        }
    }

    public void addGenres(JSONObject movieJson) throws JSONException {
        JSONArray genres = movieJson.getJSONArray("movie_genres");
        for(int i = 0; i < genres.length(); ++i)
        {
            JSONObject genre = (JSONObject) genres.get(i);
            addGenre(genre.getString("genre_name"));
        }
    }

    public void addGenres(JSONObject movieJson, int max_genres) throws JSONException {
        JSONArray genres = movieJson.getJSONArray("movie_genres");
        for(int i = 0; i < genres.length() && i < max_genres; ++i)
        {
            JSONObject genre = (JSONObject) genres.get(i);
            addGenre(genre.getString("genre_name"));
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", director='" + director + '\'' +
                ", rating=" + rating +
                '}';
    }
}