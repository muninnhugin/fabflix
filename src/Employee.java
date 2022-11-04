import com.google.gson.JsonObject;

public class Employee {
    private String email;
    private String fullName;

    Employee(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public JsonObject toJson()
    {
        JsonObject employeeJson = new JsonObject();

        employeeJson.addProperty("email", email);
        employeeJson.addProperty("full_name", fullName);

        return employeeJson;
    }

}
