import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Star {
    String id = "";
    String name = "";
    int birthYear;

    Star(String newId, String newName, int newBirthYear)
    {
        id = newId;
        name = newName;
        birthYear = newBirthYear;
    }

    Star(ResultSet rs) throws SQLException
    {
        id = rs.getString("starId");
        name = rs.getString("name");
        birthYear = rs.getInt("birthYear");
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getBirthYear() { return birthYear; }

    public JsonObject toJson()
    {
        JsonObject starJson = new JsonObject();

        starJson.addProperty("star_id", id);
        starJson.addProperty("star_name", name);
        starJson.addProperty("star_birth_year", birthYear);

        return starJson;
    }

}