import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

// This annotation maps this Java Servlet Class to a URL
@WebServlet("/single-star")
public class SingleStarServlet extends HttpServlet {
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

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // declare statement
            Statement statement = connection.createStatement();
            // prepare query
            String query = "SELECT * FROM stars WHERE id = \"" + id + "\"";
            // execute query
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();
            String starName = resultSet.getString("name");
            String starBirthYear = resultSet.getString("birthYear");

            out.println("<body>");
            out.println("<h1>" + starName +"</h1>");
            out.println("<p>Born in: " + starBirthYear);
            out.println("<p></p>");

            out.println("<table border>");

            // Add table header row
            out.println("<tr>");
            out.println("<td>Movie Name</td>");
            out.println("<td>Movie Year</td>");
            out.println("<td>Movie Director</td>");
            out.println("</tr>");

            // query for movies star has starred in
            query = "SELECT m.title, m.year, m.director\n" +
                    "FROM movies AS m, stars_in_movies AS sm\n" +
                    "WHERE sm.starId = \"" + id + "\" AND sm.movieId = m.id";
            resultSet = statement.executeQuery(query);

            // Add a row for every movie result
            while (resultSet.next()) {
                String movieTitle = resultSet.getString("title");
                String movieYear = resultSet.getString("year");
                String movieDirector = resultSet.getString("director");

                out.println("<tr>");
                out.println("<td>" + movieTitle + "</td>");
                out.println("<td>" + movieYear + "</td>");
                out.println("<td>" + movieDirector + "</td>");
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
}


