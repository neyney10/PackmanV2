package DB;

import java.sql.ResultSet;

/**
 * DBConnection data base connection
 * basic methods of data base
 */
public interface DBConnection {

    /**
     * connectToDB connect to data base
     * @return true for succeed and false for failed
     */
    public boolean connectToDB();
    /**
     * disconnectFromDB disconnect from data base
     * @return true for succeed and false for failed
     */
    public boolean disconnectFromDB();
    /**
     * getDataFromDB send query get result of query
     * @param query - query of statement
     * @return ResultSet result of query
     */
    public ResultSet getDataFromDB(String query);
}
