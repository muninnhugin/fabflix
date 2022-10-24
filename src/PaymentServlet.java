import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {
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
}
