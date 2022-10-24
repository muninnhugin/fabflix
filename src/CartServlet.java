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
import java.util.Date;
import java.util.Map;

// Declaring a WebServlet called SingleMovieServlet, which maps to url "/api/single-movie"
@WebServlet(name = "CartServlet", urlPatterns = "/api/cart")
public class CartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpSession session = request.getSession();

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }

        // write all the data into the jsonObject
        response.getWriter().write(cart.toJson().toString());
    }
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        HttpSession session = request.getSession();
//        CartItem item = new CartItem(request);
//
//        ArrayList<CartItem> cart = (ArrayList<CartItem>) session.getAttribute("cart");
//        if (cart == null)
//        {
//            cart = new ArrayList<>();
//            cart.add(item);
//            session.setAttribute("cart", cart);
//        }
//        else {
//            // prevent corrupted states through sharing under multi-threads
//            // will only be executed by one thread at a time
//            synchronized (cart) {
//                cart.add(item);
//            }
//        }
//
//        response.getWriter().write(getCartJson(cart).toString());
//    }
}
