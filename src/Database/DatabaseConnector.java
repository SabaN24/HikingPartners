package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DBConnection class which communicates with database
 *
 * @author Saba
 *
 */

public class DatabaseConnector {

    private static DatabaseConnector connector = null;

    private Connection connection;


    public static DatabaseConnector getInstance(){
        if(connector == null){
            connector = new DatabaseConnector();
        }
        return connector;
    }

    /**
     * Constructor of DatabaseConnector object
     */
    private DatabaseConnector() {
        try {
            Class.forName(DatabaseManagerConfig.DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(DatabaseManagerConfig.SERVER + DatabaseManagerConfig.NAME, DatabaseManagerConfig.USER, DatabaseManagerConfig.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getting data from database
     * @param query - SQL statement
     * @return Data depending on query
     */
    public ResultSet getData(String query) {
        ResultSet result = null;
        try {
            Statement statement = connection.createStatement();
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Updating database depending on query
     * @param query - SQL statement
     */
    public void updateData(String query) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls given procedure in database.
     * @param procedure name of procedure
     * @param parameter parameter of procedure
     * @return result of procedure
     */
    public ResultSet callProcedure(String procedure, String parameter){
        ResultSet rs = null;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("CALL " + procedure + "(" + parameter + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

}