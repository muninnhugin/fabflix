import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnInfo {
    String tableName;
    String columnName;
    String dataType;

    ColumnInfo(ResultSet resultSet) throws SQLException
    {
        tableName = resultSet.getString("TABLE_NAME");
        columnName = resultSet.getString("COLUMN_NAME");
        dataType = resultSet.getString("DATA_TYPE");
    }

    public JsonObject toJson()
    {
        JsonObject columnJson = new JsonObject();

        columnJson.addProperty("table_name", tableName);
        columnJson.addProperty("column_name", columnName);
        columnJson.addProperty("data_type", dataType);

        return columnJson;
    }
}
