import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PaymentConfirmation {
    private FormSubmitResponse status;
    private String saleId;
    private String userId;
    private Cart cart;

    PaymentConfirmation()
    {
        status = new FormSubmitResponse();
        saleId = "";
        userId = "";
        cart = new Cart();
    }

    public FormSubmitResponse getStatus() {
        return status;
    }

    public String getSaleId() {
        return saleId;
    }

    public String getUserId() {
        return userId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setStatus(FormSubmitResponse status) {
        this.status = status;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public JsonObject toJson()
    {
        JsonObject confirmationJson = new JsonObject();

        confirmationJson.addProperty("status", status.getStatus());
        confirmationJson.addProperty("message", status.getMessage());
        confirmationJson.addProperty("user_id", userId);
        confirmationJson.addProperty("sale_id", saleId);
        JsonArray cartJson = cart.toJson();
        confirmationJson.add("cart_items", cartJson);
        confirmationJson.addProperty("cart_total", cart.getTotal());

        return confirmationJson;
    }
}
