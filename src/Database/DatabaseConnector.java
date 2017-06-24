package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     * @param parameters list of parameters
     *
     * @return result of procedure
     */
    public ResultSet callProcedure(String procedure, List<String> parameters){
        ResultSet rs = null;
        try {
            Statement statement = connection.createStatement();
            StringBuilder query = new StringBuilder("CALL " + procedure + "(");
            for(int i = 0; i < parameters.size() - 1; i++){
                query.append(parameters.get(i) + ", ");
            }
            if(parameters.size() != 0) {
                query.append(parameters.get(parameters.size() - 1) + ");");
            }
            rs = statement.executeQuery(query.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /*
     * Added this 3 methods Because UserIndoDM is using PreparedStatement instead of Statement
     * Someone must change this class and all other classes which using Statement and not PreparedStatement.
     */

    /**
     * returning prepared statement depending on query
     * @param query
     * @return PreparedStatement
     */
    public PreparedStatement getPreparedStatement(String query){
        try {
            return connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updating data in database using PreparedStatement
     * @param st
     */
    public void updateDataWithPreparedStatement(PreparedStatement st){
        try {
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getting data from database using PreparedStatement
     * @param st
     * @return
     */
    public ResultSet getDataWithPreparedStatement(PreparedStatement st) {
        ResultSet result = null;
        try {
            result = st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}