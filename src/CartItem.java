import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;

public class CartItem {
    String movieTitle;
    int quantity;
    double price = 5;

    CartItem(HttpServletRequest request)
    {
        String movie_title = request.getParameter("movie_title");
        String movie_quantity = request.getParameter("movie_quantity");
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public JsonObject toJson() {
        JsonObject cartItemJson = new JsonObject();

        cartItemJson.addProperty("movie_title", movieTitle);
        cartItemJson.addProperty("quantity", quantity);
        cartItemJson.addProperty("price", price);

        return cartItemJson;
    }
}
