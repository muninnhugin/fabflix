import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Movie {
    private String id = "";
    private String title = "";
    private int year;
    private String director = "";
    private double rating;
    private ArrayList<Genre> genreList;

    Movie(String newId, String newTitle, int newYear, String newDirector, double newRating)
    {
        id = newId;
        title = newTitle;
        year = newYear;
        director = newDirector;
        rating = newRating;
        genreList = new ArrayList<>();
    }

    Movie(ResultSet rs) throws SQLException
    {
        id = rs.getString("id");
        title = rs.getString("title");
        year = rs.getInt("year");
        director = rs.getString("director");
        rating = rs.getDouble("rating");
        genreList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public double getRating() {
        return rating;
    }

    public ArrayList<Genre> getGenreList() {
        return genreList;
    }

    public JsonObject toJson() {
        JsonObject movieJson = new JsonObject();

        movieJson.addProperty("movie_id", id);
        movieJson.addProperty("movie_title", title);
        movieJson.addProperty("movie_year", year);
        movieJson.addProperty("movie_director", director);
        movieJson.addProperty("movie_rating", rating);

        return movieJson;
    }

    public Movie addGenre(Genre newGenre)
    {
        genreList.add(newGenre);
        return this;
    }
}