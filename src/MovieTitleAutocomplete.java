import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class Suggestion
{
    String value;
    String data;

    Suggestion(String value, String data)
    {
        this.value = value;
        this.data = data;
    }

    public JsonObject toJson()
    {
        JsonObject suggestionJson = new JsonObject();
        suggestionJson.addProperty("value", value);
        suggestionJson.addProperty("data", data);
        return suggestionJson;
    }
}

public class MovieTitleAutocomplete {
    String query;
    ArrayList<Suggestion> suggestions;

    MovieTitleAutocomplete(String query)
    {
        this.query = query;
        suggestions = new ArrayList<>();
    }

    public JsonArray getSuggestionsJson()
    {
        JsonArray suggestionsJson = new JsonArray();
        for(Suggestion suggestion : suggestions)
        {
            suggestionsJson.add(suggestion.toJson());
        }
        return suggestionsJson;
    }

    public JsonObject toJson()
    {
        JsonObject autocompleteJson = new JsonObject();
        autocompleteJson.addProperty("query", query);
        autocompleteJson.add("suggestions", getSuggestionsJson());
        return autocompleteJson;
    }

    public void addMovie(ResultSet rs) throws SQLException
    {
        suggestions.add(new Suggestion(rs.getString("title"), rs.getString("id")));
    }
}
