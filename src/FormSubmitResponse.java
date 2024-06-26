import com.google.gson.JsonObject;

public class FormSubmitResponse {
    private String status;
    private String message;

    private static final String SUCCESS = "success";
    private static final String FAIL = "failure";

    FormSubmitResponse()
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

    public void setSuccess(String message)
    {
        this.status = SUCCESS;
        this.message = message;
    }

    public void setFail(String message)
    {
        this.status = FAIL;
        this.message = message;
    }

    public void appendMessage(String toAppend)
    {
        message += " " + toAppend;
    }

    public JsonObject toJson()
    {
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("status", status);
        responseJson.addProperty("message", message);
        return responseJson;
    }
}
