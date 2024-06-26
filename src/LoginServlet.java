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
import org.jasypt.util.password.StrongPasswordEncryptor;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String formSource = request.getParameter("form_source");


        FormSubmitResponse loginResponse = new FormSubmitResponse();

        if(formSource.equals("website"))
        {
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
            try {
                RecaptchaVerifyUtils.verify(gRecaptchaResponse);
            } catch (Exception e) {
                loginResponse.setFail(e.getMessage());
                out.write(loginResponse.toJson().toString());
                out.close();
                response.setStatus(200);
            }
        }

        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            // Construct a query with parameter represented by "?"
            String query = "SELECT * \n" +
                    "FROM customers c \n" +
                    "WHERE c.email = ?";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            if(rs.next())
            {
                String correctPassword = rs.getString("password");
                if(!new StrongPasswordEncryptor().checkPassword(password, correctPassword))
                {
                    // Login fail
                    loginResponse.setFail("incorrect login password");
                    // Log to localhost log
                    request.getServletContext().log("Login failed");
                }
                else {
                    String userId = rs.getString("id");
                    request.getSession().setAttribute("user", new User(username, userId));
                    loginResponse.setSuccess("success");
                }
            }
            else
            {
                // Login fail
                loginResponse.setFail("incorrect login username");
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