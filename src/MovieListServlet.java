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
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // declare statement
            Statement statement = connection.createStatement();
            // prepare query
            String query = "SELECT m.id, m.title, m.year, m.director, r.rating\n" +
                    "FROM movies AS m, ratings AS r\n" +
                    "WHERE m.id = r.movieId \n" +
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
            out.println("<td>Rating</td>");
            out.println("<td>Starring</td>");
            out.println("</tr>");

            int numMoviesToDisplay = 20;

            for(int i = 0; i < numMoviesToDisplay; ++i)
            {
                resultSet.next();
                String movieID = resultSet.getString("ID");
                String movieTitle = resultSet.getString("Title");
                String movieYear = resultSet.getString("Year");
                String movieDirector = resultSet.getString("Director");
                String movieRating = resultSet.getString("Rating");
                ArrayList<String> movieGenres = getGenres(movieID);
                ArrayList<Star> starList = getStars(movieID);

                out.println("<tr>");
                //movie title is hyperlinked to single movie page
                out.println("<td>" +
                        "<a href=\"/Fabflix_war/single-movie?id=" + movieID +
                        "\">" + movieTitle + "</a></td>");
                out.println("<td>" + movieYear + "</td>");
                out.println("<td>" + movieDirector + "</td>");
                out.println("<td>" + movieRating + "</td>");
                out.println("<td>" + arrayListToString(movieGenres) + "</td>");
                out.println("<td><ul>" + getStarListHTML(starList) + "</ul></td>");
                out.println("</tr>");

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

    ArrayList<String> getGenres(String movieID)
    {
        ArrayList<String> genreList = new ArrayList<>();
        try {
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // declare statement
            Statement statement = connection.createStatement();
            String query = "SELECT *" +
                    "FROM movies m, genres_in_movies gm, genres g \n" +
                    "WHERE m.id = '" + movieID + "' AND m.id = gm.movieId AND gm.genreId = g.id";
            ResultSet genreResult = statement.executeQuery(query);
            while(genreResult.next())
            {
                genreList.add(genreResult.getString("name"));
            }
            genreResult.close();
            statement.close();
            connection.close();
        }
        catch(Exception e)
        {
            log("Error in function getStars");
        }
        log(genreList.toString());
        return genreList;
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

    // TODO: refactor helper functions so that all files can access it
    String arrayListToString(ArrayList<String> list) {
        String s = list.toString();
        s = s.substring(1, s.length() - 1);
        return s;
    }

}
