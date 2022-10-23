import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called SingleMovieServlet, which maps to url "/api/single-movie"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/Fabflix");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        // The log message can be found in localhost log
        request.getServletContext().log("getting id: " + id);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            // Construct a query with parameter represented by "?"
            String query = "SELECT m.id AS movieId, m.title, m.year, m.director, r.rating \n" +
                    "FROM movies m, ratings r\n" +
                    "WHERE m.id = ? AND r.movieId = m.id";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            rs.next();
            Movie movie = new Movie(rs);

            // get movie genres
            query = "SELECT *\n" +
                    "FROM movies m, genres_in_movies gm, genres g\n" +
                    "WHERE m.id = ? AND m.id = gm.movieId AND gm.genreId = g.id\n" +
                    "ORDER BY g.name ASC";
            statement = conn.prepareStatement(query);
            statement.setString(1, id);
            rs = statement.executeQuery();
            while(rs.next()) {
                Genre genre = new Genre(rs);
                movie.addGenre(genre);
            }

            // get stars information
            query = "SELECT starId, s.name, s.birthYear, COUNT(movieId)\n" +
                    "FROM stars_in_movies, stars s\n" +
                    "WHERE starId IN (SELECT sm.starId\n" +
                    "\t\t\t\tFROM movies m, stars_in_movies sm, stars s\n" +
                    "\t\t\t\tWHERE m.id = ? AND m.id = sm.movieId AND sm.starId = s.id)\n" +
                    "\tAND starId = s.id\n" +
                    "GROUP BY starId\n" +
                    "ORDER BY COUNT(movieId) DESC, name ASC";
            statement = conn.prepareStatement(query);
            statement.setString(1, id);
            rs = statement.executeQuery();
            while(rs.next())
            {
                Star star = new Star(rs);
                movie.addStar(star);
            }

            JsonObject movieJson = movie.toJson();

            rs.close();
            statement.close();

            // Write JSON string to output
            out.write(movieJson.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Log error to localhost log
            request.getServletContext().log("Error:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }

}
