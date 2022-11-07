import com.google.gson.JsonArray;

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

import static java.sql.Types.INTEGER;
import static java.sql.Types.VARCHAR;

@WebServlet(name = "EmployeeDashboardServlet", urlPatterns = "/api/employee_dashboard")
public class EmployeeDashboardServlet extends HttpServlet {
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/Fabflix");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        String formType = request.getParameter("form_type");

        FormSubmitResponse postResponse = new FormSubmitResponse();

        try (Connection connection = dataSource.getConnection())
        {
            if(formType.equals("Add Star Form"))
            {
                postResponse = insertStar(request, connection);
            }
            else if(formType.equals("Add Movie Form"))
            {
                postResponse = insertMovie(request, connection);
            }

            // Set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {

            postResponse.setFail(e.getMessage());
            request.getServletContext().log("Error:", e);
            response.setStatus(500);

        } finally {
            out.write(postResponse.toJson().toString());
            out.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        try (Connection connection = dataSource.getConnection())
        {
            String query = "SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE\n" +
                    "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                    "WHERE TABLE_SCHEMA = 'Fabflix'\n";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            JsonArray columnsJson = new JsonArray();

            while(resultSet.next())
            {
                ColumnInfo columnInfo = new ColumnInfo(resultSet);
                columnsJson.add(columnInfo.toJson());
            }

            out.write(columnsJson.toString());

            response.setStatus(200);

        } catch (Exception e) {

            request.getServletContext().log("Error:", e);

            FormSubmitResponse getResponse = new FormSubmitResponse();
            getResponse.setFail(e.getMessage());
            out.write(getResponse.toJson().toString());

            response.setStatus(200);

        } finally {
            out.close();
        }
    }

    private FormSubmitResponse insertStar(HttpServletRequest request, Connection connection) throws SQLException {
        FormSubmitResponse response = new FormSubmitResponse();

        String starId = Helper.getNewStarId(connection);
        String starName = request.getParameter("star_name");
        String birthYear = request.getParameter("birth_year");

        String query = "INSERT INTO stars \n" +
                "VALUES (?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, starId);
        statement.setString(2, starName);
        if(Helper.isValid(birthYear))
        {
            statement.setInt(3, Integer.parseInt(birthYear));
        }
        else
        {
            statement.setNull(3, INTEGER);
        }

        int rowsAffected = statement.executeUpdate();

        if (rowsAffected == 0)
        {
            response.setFail("Failed to insert star to db.");
        }
        else
        {
            response.setSuccess("Insert star to db successful.");
        }

        return response;
    }

    private FormSubmitResponse insertMovie(HttpServletRequest request, Connection connection) throws SQLException {
        FormSubmitResponse response = new FormSubmitResponse();

        String movieTitle = request.getParameter("movie_title");
        String movieYear = request.getParameter("movie_year");
        String movieDirector = request.getParameter("movie_director");
        String genreName = request.getParameter("genre_name");
        String starName = request.getParameter("star_name");
        String starYear = request.getParameter("star_year");

        String query = "CALL add_movie(?, ?, ?, ?, ?, ?, ?, ?); \n";

        CallableStatement procedure = connection.prepareCall(query);
        procedure.setString(1, movieTitle);
        procedure.setInt(2, Integer.parseInt(movieYear));
        procedure.setString(3, movieDirector);
        procedure.setString(4, genreName);
        procedure.setString(5, starName);
        if(Helper.isValid(starYear))
        {
            procedure.setInt(6, Integer.parseInt(starYear));
        }
        else
        {
            procedure.setNull(6, INTEGER);
        }
        procedure.registerOutParameter(7, VARCHAR);
        procedure.registerOutParameter(8, VARCHAR);

        procedure.execute();
        String message = procedure.getString(8);
        response.setSuccess(message);

        return response;
    }


}
