import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

// This annotation maps this Java Servlet Class to a URL
@WebServlet("/single-movie")
public class SingleMovieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String loginUser = "mytestuser";
    private static final String loginPasswd = "My6$Password";
    private static final String loginUrl = "jdbc:mysql://localhost:3306/Fabflix";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Set response mime type
        response.setContentType("text/html");

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        // The log message can be found in localhost log
        request.getServletContext().log("getting id: " + id);

        // Get the PrintWriter for writing response
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Fabflix</title></head>");
        out.println("<p><a href=\"/Fabflix_war/movie-list\"> Top Movies </a></p>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // declare statement
            Statement statement = connection.createStatement();
            // prepare query
            String query = "SELECT * \n" +
                    "FROM movies m, ratings r\n" +
                    "WHERE m.id = \"" + id + "\" AND r.movieId = m.id";
            // execute query
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();
            String movieTitle = resultSet.getString("title");
            String movieYear = resultSet.getString("year");
            String movieDirector = resultSet.getString("director");
            String movieRating = resultSet.getString("rating");
            String movieGenres = arrayListToString(getGenres(id));
            ArrayList<Star> movieStars = getStars(id);

            out.println("<body>");
            out.println("<h1>Movie Details</h1>");
            out.println("<p>Title: " + movieTitle + "</p>");
            out.println("<p>Year: " + movieYear + "</p>");
            out.println("<p>Director: " + movieDirector + "</p>");
            out.println("<p>Rating: " + movieRating + "</p>");
            out.println("<p>Genres: " + movieGenres + "</p>");
            out.println("<p>Cast: " + getStarListHTML(movieStars) + "</p>");
            out.println("<p></p>");

            out.println("<table border>");
            out.println("<tr>");

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            /*
             * After you deploy the WAR file through tomcat manager webpage,
             *   there's no console to see the print messages.
             * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
             *
             * To view the last n lines (for example, 100 lines) of messages you can use:
             *   tail -100 catalina.out
             * This can help you debug your program after deploying it on AWS.
             */
            request.getServletContext().log("Error: ", e);

            out.println("<body>");
            out.println("<p>");
            out.println("Exception in doGet: " + e.getMessage());
            out.println("</p>");
            out.print("</body>");
        }

        out.println("</html>");
        out.close();
    }

    // function getGenres
    // input: movieID
    // output: ArrayList of genres
    ArrayList<String> getGenres (String movieId) {
        ArrayList<String> genres = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            Statement statement = connection.createStatement();
            String query = "SELECT *\n" +
                    "FROM movies m, genres_in_movies gm, genres g\n" +
                    "WHERE m.id = \'" + movieId + "\' AND m.id = gm.movieId AND gm.genreId = g.id";
            ResultSet genreResultSet = statement.executeQuery(query);

            while(genreResultSet.next())
            {
                genres.add(genreResultSet.getString("name"));
            }

            genreResultSet.close();
            connection.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }

    ArrayList<Star> getStars(String movieID)
    {
        ArrayList<Star> starList = new ArrayList<>();
        try {
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // declare statement
            Statement statement = connection.createStatement();
            String query = "SELECT *" +
                    "FROM movies m, stars_in_movies sm, stars s \n" +
                    "WHERE m.id = '" + movieID + "' AND m.id = sm.movieId AND sm.starId = s.id";
            ResultSet starResult = statement.executeQuery(query);
            while(starResult.next())
            {
                String starId = starResult.getString("starId");
                String starName = starResult.getString("name");
                int starBirthYear = starResult.getInt("birthYear");
                Star star = new Star(starId, starName, starBirthYear);
                starList.add(star);
            }
            starResult.close();
            statement.close();
            connection.close();
        }
        catch(Exception e)
        {
            log("Error in function getStars");
        }
        log(starList.toString());
        return starList;
    }

    String getStarListHTML(ArrayList<Star> starList) {
        String s = "";
        int listItemLimit = 3;
        for(int i = 0; i < listItemLimit; ++i)
        {
            Star star = starList.get(i);
            String starId = star.getId();
            String starName = star.getName();
            s += "<li><a href=\"/Fabflix_war/single-star?id=" + starId +
                    "\">" + starName + "</a></li>\n";
        }
        return s;
    }

    String arrayListToString(ArrayList<String> list) {
        String s = list.toString();
        s = s.substring(1, s.length() - 1);
        return s;
    }
}


