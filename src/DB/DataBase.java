package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *  DataBase :
 *  connect to a SQL-Based database, formally built for MySql
 */
public class DataBase implements DBConnection {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    //////// Connection details
    private String DBUrl;
    private String username;
    private String password;

    /**
     * [Constructor] <br>
     * @param jdbcUrl - the DB url connection
     * @param username - the user name to the DB
     * @param password - the password matching the corresponding username to THE db.
     */
    public DataBase(String jdbcUrl, String username, String password) {
        this.DBUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    /**
     * connectToDB connect to data base
     * @return true for succeed and false for failed
     */
    @Override
    public boolean connectToDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DBUrl, username, password);
        }catch (Exception e){
         return false;
        }
        return true;
    }

    /**
     * disconnectFromDB disconnect from data base
     * @return true for succeed and false for failed
     */
    @Override
    public boolean disconnectFromDB() {
        try {
            if ( resultSet!=null && !resultSet.isClosed())
                 resultSet.close();
            if (statement!=null && !statement.isClosed())
                  statement.close();
            if (connection!=null && !connection.isClosed())
                 connection.close();
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * getDataFromDB send query get result of query
     * @param query - query of statement
     * @return ResultSet result of query
     */
    @Override
    public ResultSet getDataFromDB(String query) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        }catch (Exception e){
            return null;
        }
        return resultSet;
    }
}
