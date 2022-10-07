import javax.management.Query;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

// This annotation maps this Java Servlet Class to a URL
@WebServlet("/movie-list")
public class MovieListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String loginUser = "mytestuser";
    private static final String loginPasswd = "My6$Password";
    private static final String loginUrl = "jdbc:mysql://localhost:3306/Fabflix";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Set response mime type
        response.setContentType("text/html");

        // Get the PrintWriter for writing response
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Fabflix</title></head>");

        try {
            // TODO for each movie do a separate query for stars
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // declare statement
            Statement statement = connection.createStatement();
            // prepare query
            String query = "SELECT m.id, m.title, m.year, m.director, r.rating, g.name AS genres\n" +
                    "FROM movies AS m, ratings AS r, genres_in_movies AS gm, genres AS g\n" +
                    "WHERE m.id = r.movieId AND m.id = gm.movieId AND gm.genreID = g.id \n" +
                    "ORDER BY r.rating DESC";
            // execute query
            ResultSet resultSet = statement.executeQuery(query);

            out.println("<body>");
            out.println("<h1>Top Rated Movies</h1>");

            out.println("<table border>");

            // Add table header row
            out.println("<tr>");
            out.println("<td>Title</td>");
            out.println("<td>Year</td>");
            out.println("<td>Director</td>");
            out.println("<td>Genres</td>");
            out.println("<td>Rating</td>");
            out.println("<td>Starring</td>");
            out.println("</tr>");

            resultSet.next();
            int numMoviesToDisplay = 20;
            int curNumMovies = 1;
            int curNumGenres = 1;
            String prevMovieID = "";
            String movieID = resultSet.getString("ID");
            String movieTitle = resultSet.getString("Title");
            String movieYear = resultSet.getString("Year");
            String movieDirector = resultSet.getString("Director");
            String movieGenre = resultSet.getString("Genres");
            String movieGenres = movieGenre;
            String movieRating = resultSet.getString("Rating");
            ArrayList<String> starList = getStars(movieID);

            // Add a row for every star result
            while (resultSet.next() && curNumMovies < numMoviesToDisplay ) {
                // get a star from result set

                if(movieID.equals(prevMovieID))
                {
                    // check for extra genres
                    if(!movieGenres.contains(movieGenre) && curNumGenres < 3) {
                        movieGenres += ", " + movieGenre;
                        ++curNumGenres;
                    }
                }
                else {
                    ++curNumMovies;
                    prevMovieID = movieID;

                    out.println("<tr>");
                    out.println("<td>" + movieTitle + "</td>");
                    out.println("<td>" + movieYear + "</td>");
                    out.println("<td>" + movieDirector + "</td>");
                    out.println("<td>" + movieGenres + "</td>");
                    out.println("<td>" + movieRating + "</td>");
                    out.println("<td>" + convertToString(starList) + "</td>");
                    out.println("</tr>");
                }

                movieID = resultSet.getString("ID");
                movieTitle = resultSet.getString("Title");
                movieYear = resultSet.getString("Year");
                movieDirector = resultSet.getString("Director");
                movieGenre = resultSet.getString("Genres");
                movieRating = resultSet.getString("Rating");
                starList = getStars(movieID);
            }

            out.println("</table>");
            out.println("</body>");

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

        ArrayList<String> getStars(String movieID)
        {
            ArrayList<String> starList = new ArrayList<String>();
            try {
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                // create database connection
                Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
                // declare statement
                Statement statement = connection.createStatement();
                String query = "SELECT s.name AS stars_name\n" +
                        "FROM movies m, stars_in_movies sm, stars s \n" +
                        "WHERE m.id = '" + movieID + "' AND m.id = sm.movieId AND sm.starId = s.id";
                ResultSet starResult = statement.executeQuery(query);
                while(starResult.next())
                {
                    starList.add(starResult.getString("stars_name"));
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

        String convertToString(ArrayList<String> list) {
            String s = list.subList(0, 3).toString();
            s = s.substring(1, s.length() - 1);
            return s;
        }

}
