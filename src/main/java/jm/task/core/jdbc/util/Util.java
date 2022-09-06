package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static Connection cachedConnection;
    private static final String MYSQL_URL_PREFIX = "jdbc:mysql://";
    private static final String MYSQL_PORT_STRING = ":3306/";
    private static final String HOST_NAME = "localhost";
    private static final String DB_NAME = "kata_db";
    private static final String USER_NAME = "katauser";
    private static final String PASSWORD = "katauser";

    public static Connection getConnection() throws SQLException {
        return getConnection(HOST_NAME, DB_NAME, USER_NAME, PASSWORD);
    }

    public static Connection getConnection(
            String hostName, String dbName, String userName, String password) throws SQLException {
        if (cachedConnection == null || cachedConnection.isClosed()) {
            cachedConnection = DriverManager.getConnection(
                    MYSQL_URL_PREFIX + HOST_NAME + MYSQL_PORT_STRING + DB_NAME,
                    USER_NAME,
                    PASSWORD);
        }
        return cachedConnection;
    }
}
