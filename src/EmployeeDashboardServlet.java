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
import java.sql.SQLException;

import static java.sql.Types.INTEGER;

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
}
