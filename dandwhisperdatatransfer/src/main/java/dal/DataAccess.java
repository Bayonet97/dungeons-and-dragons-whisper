package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataAccess {
    //TODO: HIDE THIS STRING LOL
    protected String  connectionUrl = "jdbc:sqlserver://mssql.fhict.local;databaseName=dbi397017;";
    protected Connection getConnection(){
        try {
            return DriverManager.getConnection(connectionUrl, "dbi397017", "i397017");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
