import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Helper {

    public static String getLatestStarIdStr(Connection connection) throws SQLException
    {
        String query = "SELECT max(id) FROM stars";

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        return rs.getString("id");
    }

    public static int getLatestStarIdInt(Connection connection) throws SQLException
    {
        String starId = getLatestStarIdStr(connection);
        return Integer.parseInt(starId.substring(2));
    }

    public static String getNewStarId(Connection connection) throws SQLException
    {
        return "nm" + (getLatestStarIdInt(connection) + 1);
    }

    public static boolean isValid(String param)
    {
        return param != null && !param.isEmpty();
    }
}
