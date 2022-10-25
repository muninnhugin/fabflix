import com.google.gson.JsonObject;

/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class User {

    private String username;
    private String id;

    public User(String username, String id) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public JsonObject toJson()
    {
        JsonObject userJson = new JsonObject();

        userJson.addProperty("username", username);
        userJson.addProperty("user_id", id);

        return userJson;
    }

}