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

    String getId()          { return id; }
    String getTitle()       { return title; }
    int getYear()           { return year; }
    String getDirector()    { return director; }

    String getHyperlinkedMovieTitle() {
        String s = "";
        String movieId = getId();
        String movieTitle = getTitle();
        s = "<li><a href=\"/Fabflix/single-movie?id=" + movieId +
                "\">" + movieTitle + "</a></li>\n";
        return s;
    }
}
