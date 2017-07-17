package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Saba on 7/18/2017.
 */
public class DatabaseHelper {


    /**
     * Method returns id of newly added row in given table.
     * @param table name of table.
     * @return id of newly added row
     * @throws SQLException in  case of any errors within database
     */
    public static int getRecentlyAdded(String table) throws SQLException {
        ResultSet resultSet = DatabaseConnector.getInstance().getData("select ID from " + table + " order by ID desc limit 1");
        if (resultSet.next()) {
            return resultSet.getInt("ID");
        }
        return -1;
    }
}
