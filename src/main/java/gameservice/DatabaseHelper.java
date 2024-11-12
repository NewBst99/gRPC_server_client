package gameservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    //    please add "???????:mysql://your database ip:port/databse name" in URL
    private static final String URL = "your URL";
    private static final String USER = "your user name";
    private static final String PASSWORD = "your password";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
