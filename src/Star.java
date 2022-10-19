import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Star {
    String id = "";
    String name = "";
    int birthYear;
    ArrayList<Movie> movieList;

    Star(String newId, String newName, int newBirthYear)
    {
        id = newId;
        name = newName;
        birthYear = newBirthYear;
        movieList = new ArrayList<>();
    }

    // TODO add code to check null values (SQL is null)
    Star(ResultSet rs) throws SQLException
    {
        id = rs.getString("starId");
        name = rs.getString("name");
        birthYear = rs.getInt("birthYear");
        movieList = new ArrayList<>();
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getBirthYear() { return birthYear; }

    public Star addMovie(Movie movie)
    {
        movieList.add(movie);
        return this;
    }

    public Star setStarInfo(ResultSet rs) throws SQLException
    {
        id = rs.getString("starId");
        name = rs.getString("name");
        birthYear = rs.getInt("birthYear");
        movieList = new ArrayList<>();
        return this;
    }

    public JsonObject toJson()
    {
        JsonObject starJson = new JsonObject();

        starJson.addProperty("star_id", id);
        starJson.addProperty("star_name", name);
        starJson.addProperty("star_birth_year", birthYear);
        starJson.add("star_movies", getMovieJsonArray());

        return starJson;
    }

    public JsonArray getMovieJsonArray() {
        JsonArray movieJsons = new JsonArray();
        for (Movie movie: movieList)
        {
            JsonObject genreJson = movie.toJson();
            movieJsons.add(genreJson);
        }
        return movieJsons;
    }

}