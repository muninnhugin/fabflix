import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;

@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/Fabflix");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpSession session = request.getSession();

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        JsonObject totalJson = new JsonObject();
        totalJson.addProperty("cart_total", cart.getTotal());
        // write all the data into the jsonObject
        response.getWriter().write(totalJson.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        CreditCardInfo enteredCcInfo = new CreditCardInfo(request);

        PaymentConfirmation paymentConfirmation = new PaymentConfirmation();
        FormSubmitResponse paymentResponse = new FormSubmitResponse();
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            // Construct a query with parameter represented by "?"
            String query = "SELECT *\n" +
                    "FROM creditcards cc\n" +
                    "WHERE cc.id = ?";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, enteredCcInfo.getCcId());
            ResultSet rs = statement.executeQuery();

            if(rs.next())
            {
                String correctFirstName = rs.getString("firstName");
                String correctLastName = rs.getString("lastName");
                String correctExpiration = rs.getString("expiration");
                CreditCardInfo correctInfo =
                        new CreditCardInfo(correctFirstName, correctLastName, enteredCcInfo.getCcId(), correctExpiration);
                if(enteredCcInfo.isMatching(correctInfo))
                {
                    paymentConfirmation = insertSale(request);
                }
                else {

                    request.getServletContext().log("Payment information not matching");
                    paymentResponse.setFail("Incorrect payment information. Please re-enter.");
                    paymentConfirmation.setStatus(paymentResponse);
                }
            }
            else
            {
                paymentResponse.setFail("cannot find matching credit card number");
                request.getServletContext().log("Payment failed: no such credit card number. Please re-enter.");
                paymentConfirmation.setStatus(paymentResponse);
            }

            rs.close();
            statement.close();

            out.write(paymentConfirmation.toJson().toString());

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

    private PaymentConfirmation insertSale(HttpServletRequest request) throws SQLException
    {
        PaymentConfirmation confirmation = new PaymentConfirmation();
        FormSubmitResponse status = new FormSubmitResponse();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");

        String query = "INSERT INTO sales(customerId, movieId, saleDate) \n" +
                "VALUES (?, ?, current_date ());";
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getId());

        for(CartItem item : cart.getCart())
        {
            statement.setString(2, item.getMovieId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0)
            {
                status.setSuccess("purchase success");
                confirmation.setStatus(status);
                confirmation.setUserId(user.getId());

                // get last sale id
                query = "SELECT *\n" +
                        "FROM sales\n" +
                        "ORDER BY id DESC\n" +
                        "LIMIT 1;";
                PreparedStatement saleStatement = connection.prepareStatement(query);
                ResultSet rs = saleStatement.executeQuery();
                rs.next();
                confirmation.setSaleId(rs.getString("id"));
                confirmation.setCart(cart);
            }
            else
            {
                status.setFail("payment validation success. purchase fail");
                confirmation.setStatus(status);
            }
        }

        session.setAttribute("cart", new Cart());

        return confirmation;
    }
}
