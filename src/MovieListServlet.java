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
import java.sql.ResultSet;
import java.sql.Statement;


// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "MovieListServlet", urlPatterns = "/api/movie-list")
public class MovieListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/Fabflix");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {

            // Declare our statement
            Statement statement = conn.createStatement();

            String query = "SELECT m.id, m.title, m.year, m.director, r.rating\n" +
                    "FROM movies AS m, ratings AS r\n" +
                    "WHERE m.id = r.movieId \n" +
                    "ORDER BY r.rating DESC";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            JsonArray movieJsons = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {
                String movieId = rs.getString("id");
                String movieTitle = rs.getString("Title");
                String movieYear = rs.getString("Year");
                String movieDirector = rs.getString("Director");
                String movieRating = rs.getString("Rating");

                // Create a JsonObject based on the data we retrieve from rs
                JsonObject movieJson = new JsonObject();
                movieJson.addProperty("movie_id", movieId);
                movieJson.addProperty("movie_title", movieTitle);
                movieJson.addProperty("movie_year", movieYear);
                movieJson.addProperty("movie_director", movieDirector);
                movieJson.addProperty("movie_rating", movieRating);

                movieJsons.add(movieJson);
            }
            rs.close();
            statement.close();

            // Log to localhost log
            request.getServletContext().log("getting " + movieJsons.size() + " results");

            // Write JSON string to output
            out.write(movieJsons.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {

            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }
}
