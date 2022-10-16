public class Movie {
    private String id = "";
    private String title = "";
    private int year;
    private String director = "";
    private double rating;

    Movie(String newId, String newTitle, int newYear, String newDirector, double newRating)
    {
        id = newId;
        title = newTitle;
        year = newYear;
        director = newDirector;
        rating = newRating;
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
}