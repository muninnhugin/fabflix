import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// This annotation maps this Java Servlet Class to a URL
@WebServlet("/movie_list")
public class MovieListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Change this to your own mysql username and password
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/Fabflix";

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
                    "WHERE m.id = r.movieId\n" +
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
            out.println("</tr>");

            int numMoviesToDisplay = 20;
            int curNumMovies = 0;

            // Add a row for every star result
            while (resultSet.next() && curNumMovies < numMoviesToDisplay ) {
                // get a star from result set
                String movieID = resultSet.getString("ID");
                String movieTitle = resultSet.getString("Title");
                String movieYear = resultSet.getString("Year");
                String movieDirector = resultSet.getString("Director");
                String movieRating = resultSet.getString("Rating");

                out.println("<tr>");
                out.println("<td>" + movieTitle+ "</td>");
                out.println("<td>" + movieYear + "</td>");
                out.println("<td>" + movieDirector + "</td>");
                out.println("<td>" + movieRating + "</td>");
                out.println("</tr>");
                ++curNumMovies;
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


}
