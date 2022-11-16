import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/api/movie_title_autocomplete")
public class AutocompleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final int MAX_SUGGESTIONS = 10;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/Fabflix");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = dataSource.getConnection()) {

            String query = request.getParameter("query");
            String[] tokens = query.split(" ");
            String statementStr = null;

            if(tokens.length > 1) {
                 statementStr = "SELECT id, title\n" +
                         "FROM movies\n" +
                         "WHERE MATCH(title) AGAINST('";
                for (String token : tokens) {
                    statementStr += "+" + token + "* ";
                }
                statementStr += "' IN BOOLEAN MODE)\n" +
                        "LIMIT " + MAX_SUGGESTIONS;
            }
            else
            {
                statementStr = "SELECT id, title\n" +
                        "FROM movies\n" +
                        "WHERE MATCH(title) AGAINST('" + tokens[0] + "') " +
                        "OR MATCH(title) AGAINST('" + tokens[0] + "*' IN BOOLEAN MODE) \n" +
                        " LIMIT " + MAX_SUGGESTIONS;
            }


            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(statementStr);

            MovieTitleAutocomplete movieTitleAutocomplete = new MovieTitleAutocomplete(query);
            while (resultSet.next()) {
                movieTitleAutocomplete.addMovie(resultSet);
            }

            response.getWriter().write(movieTitleAutocomplete.getSuggestionsJson().toString());

        } catch (Exception e) {
            System.out.println(e);
            response.sendError(500, e.getMessage());
        }
    }
}
