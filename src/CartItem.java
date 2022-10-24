import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;

public class CartItem {
    private String movieId;
    private int quantity;
    private double price = 5;

    CartItem(String newId, int newQuantity)
    {
        movieId = newId;
        quantity = newQuantity;
    }

    CartItem(HttpServletRequest request)
    {
        movieId = request.getParameter("movie_id");
        quantity = Integer.parseInt(request.getParameter("quantity"));
    }

    public String getMovieId() {
        return movieId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public JsonObject toJson()
    {
        JsonObject itemJson = new JsonObject();

        itemJson.addProperty("movie_id", movieId);
        itemJson.addProperty("quantity", quantity);
        itemJson.addProperty("price", price);

        return itemJson;
    }

    public void addOne()
    {
        ++quantity;
    }
}
