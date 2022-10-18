import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Genre {
    private String id = "";
    private String name = "";

    Genre(String newId, String newName)
    {
        id = newId;
        name = newName;
    }

    Genre(ResultSet rs) throws SQLException
    {
        id = rs.getString("genreId");
        name = rs.getString("name");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public JsonObject toJson()
    {
        JsonObject genreJson = new JsonObject();

        genreJson.addProperty("genre_id", id);
        genreJson.addProperty("genre_name", name);

        return genreJson;
    }
}
