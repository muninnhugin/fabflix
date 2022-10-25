import javax.servlet.http.HttpServletRequest;

// TODO check if all class's data members are private
public class CreditCardInfo {
    private String firstName;
    private String lastName;
    private String ccId;
    private String expiration;

    CreditCardInfo(String newFname, String newLname, String newId, String newExpiration)
    {
        firstName = newFname;
        lastName = newLname;
        ccId = newId;
        expiration = newExpiration;
    }

    CreditCardInfo(HttpServletRequest request)
    {
        firstName = request.getParameter("first_name");
        lastName = request.getParameter("last_name");
        ccId = request.getParameter("credit_card_num");
        expiration = request.getParameter("expiration_date");
    }

    public String getCcId() {
        return ccId;
    }

    public boolean isMatching(CreditCardInfo toCompare)
    {
        return firstName.equals(toCompare.firstName) &&
                lastName.equals(toCompare.lastName) &&
                ccId.equals(toCompare.ccId) &&
                expiration.equals(toCompare.expiration);
    }
}
