import com.google.gson.JsonObject;

public class LoginResponse {
    private String status;
    private String message;

    private static final String LOGIN_SUCCESS = "success";
    private static final String LOGIN_FAIL = "fail";

    LoginResponse()
    {
        status = "";
        message = "";
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public void setLoginSuccess(String message)
    {
        status = LOGIN_SUCCESS;
        this.message = message;
    }

    public void setLoginFail(String message)
    {
        status = LOGIN_FAIL;
        this.message = message;
    }

    public JsonObject toJson()
    {
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("status", status);
        responseJson.addProperty("message", message);
        return responseJson;
    }
}
