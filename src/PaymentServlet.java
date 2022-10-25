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

        double cartTotal = 0;
        for(CartItem item: cart.getCart())
        {
            cartTotal += item.getPrice() * item.getQuantity();
        }

        JsonObject totalJson = new JsonObject();
        totalJson.addProperty("cart_total", cartTotal);
        // write all the data into the jsonObject
        response.getWriter().write(totalJson.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        CreditCardInfo enteredCcInfo = new CreditCardInfo(request);

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
                    paymentResponse.setSuccess("correct payment information");
                    // TODO call and implement insertSale()
                }
                else {

                    request.getServletContext().log("Payment information not matching");
                    paymentResponse.setFail("incorrect payment information");
                }
            }
            else
            {
                paymentResponse.setFail("cannot find matching credit card number");
                request.getServletContext().log("Payment failed: no such credit card number ");
            }

            rs.close();
            statement.close();

            out.write(paymentResponse.toJson().toString());

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
