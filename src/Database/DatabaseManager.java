package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DBConnection class which communicates with database
 *
 * @author Saba
 *
 */

public class DatabaseManager {

    private Connection connection;
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String USER = "";
    private static final String DATABASE = "jdbc:mysql://localhost:3306/Hiking_Partners";
    private static final String PASSWORD = "";

    /**
     * Constructor of DatabaseManager object
     */
    public DatabaseManager() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
