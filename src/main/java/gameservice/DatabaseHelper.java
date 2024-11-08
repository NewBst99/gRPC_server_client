package gameservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    //    please add "???????:mysql://your database ip/databse name" in URL
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/CapstoneDB";
    private static final String USER = "root";
    private static final String PASSWORD = "zoqtmxhs0716!";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
