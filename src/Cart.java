import com.google.gson.JsonArray;

import java.util.ArrayList;

// TODO add some common arrayList function wrappers: add, get, etc.

public class Cart {
    ArrayList<CartItem> cart;

    Cart()
    {
        cart = new ArrayList<>();
    }

    public ArrayList<CartItem> getCart() {
        return cart;
    }

    public JsonArray toJson()
    {
        JsonArray cartJson = new JsonArray();

        for(CartItem cartItem : cart)
        {
            cartJson.add(cartItem.toJson());
        }

        return cartJson;
    }
}
