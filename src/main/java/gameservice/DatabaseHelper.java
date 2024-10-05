package gameservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

//    please add "???????:mysql://your database ip/databse name" in URL
    private static final String URL = "";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
