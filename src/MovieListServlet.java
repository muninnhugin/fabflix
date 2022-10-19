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
import java.sql.*;


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

            Statement statement = conn.createStatement();

            String query = "SELECT m.id AS movieId, m.title, m.year, m.director, r.rating\n" +
                    "FROM movies AS m, ratings AS r\n" +
                    "WHERE m.id = r.movieId \n" +
                    "ORDER BY r.rating DESC";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            JsonArray movieJsons = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {
                Movie movie = new Movie(rs);

                // add genre information
                query = "SELECT *\n" +
                        "FROM movies m, genres_in_movies gm, genres g\n" +
                        "WHERE m.id = '" + movie.getId() + "'AND m.id = gm.movieId AND gm.genreId = g.id";

                Statement subStatement = conn.createStatement();
                ResultSet subRs = subStatement.executeQuery(query);

                while(subRs.next()) {
                    Genre genre = new Genre(subRs);
                    movie.addGenre(genre);
                }

                // add star infoz
                query = "SELECT *" +
                        "FROM movies m, stars_in_movies sm, stars s \n" +
                        "WHERE m.id = '" + movie.getId() + "' AND m.id = sm.movieId AND sm.starId = s.id";
                subStatement = conn.createStatement();
                subRs = subStatement.executeQuery(query);
                while(subRs.next())
                {
                    Star star = new Star(subRs);
                    movie.addStar(star);
                }

                subStatement.close();
                subRs.close();

                JsonObject movieJson = movie.toJson();
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

    private Movie getGenres(Movie movie) throws SQLException
    {
        String query = "SELECT *\n" +
                "FROM movies m, genres_in_movies gm, genres g\n" +
                "WHERE m.id = ? AND m.id = gm.movieId AND gm.genreId = g.id";

        Connection conn = dataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, movie.getId());
        ResultSet rs = statement.executeQuery();

        while(rs.next()) {
            Genre genre = new Genre(rs);
            movie.addGenre(genre);
        }

        conn.close();
        statement.close();
        rs.close();

        return movie;
    }
}
