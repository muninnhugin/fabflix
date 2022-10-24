import com.google.gson.JsonArray;
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
import java.util.ArrayList;

// Declaring a WebServlet called SingleMovieServlet, which maps to url "/api/single-movie"
@WebServlet(name = "CartServlet", urlPatterns = "/api/cart")
public class CartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CartItem cartItem = new CartItem(request);
        System.out.println(cartItem.toJson());
        HttpSession session = request.getSession();

        // get the previous items in a ArrayList
        ArrayList<CartItem> cart = (ArrayList<CartItem>) session.getAttribute("cart");
        if (cart == null)
        {
            cart = new ArrayList<CartItem>();
            cart.add(cartItem);
            session.setAttribute("cart", cart);
        }
        else {
            // prevent corrupted states through sharing under multi-threads
            // will only be executed by one thread at a time
            synchronized (cart) {
                cart.add(cartItem);
            }
        }

        response.getWriter().write(getCartJson(cart).toString());
    }

    private JsonArray getCartJson(ArrayList<CartItem> cart)
    {
        JsonArray cartJson = new JsonArray();
        for(CartItem cartItem : cart)
        {
            cartJson.add(cartItem.toJson());
        }
        return cartJson;
    }
}
