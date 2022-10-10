public class Movie {
    String id = "";
    String title = "";
    int year;
    String director = "";

    Movie(String newId, String newTitle, int newYear, String newDirector)
    {
        id = newId;
        title = newTitle;
        year = newYear;
        director = newDirector;
    }

    //TODO add constructor that takes ResultSet.next()

    String getId()      { return id; }
    String getTitle()   { return title; }
}
