import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

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

@WebServlet(name = "EmployeeLoginServlet", urlPatterns = "/api/employee_login")
public class EmployeeLoginServlet extends HttpServlet {
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

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        FormSubmitResponse loginResponse = new FormSubmitResponse();

        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT * \n" +
                    "FROM employees e \n" +
                    "WHERE e.email = ?";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            if(rs.next())
            {
                String correctPassword = rs.getString("password");
                if(!new StrongPasswordEncryptor().checkPassword(password, correctPassword))
                {
                    loginResponse.setFail("incorrect login password");
                    request.getServletContext().log("Login failed");
                }
                else {
                    String fullName = rs.getString("fullname");
                    request.getSession().setAttribute("employee", new Employee(email, fullName));
                    loginResponse.setSuccess("login successful");
                }
            }
            else
            {
                // Login fail
                loginResponse.setFail("incorrect login email");
                // Log to localhost log
                request.getServletContext().log("Login failed");
            }

            rs.close();
            statement.close();

            out.write(loginResponse.toJson().toString());

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
    }
}
