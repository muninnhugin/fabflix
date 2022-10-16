public class Genre {
    private String id = "";
    private String name = "";

    Genre(String newId, String newName)
    {
        id = newId;
        name = newName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
