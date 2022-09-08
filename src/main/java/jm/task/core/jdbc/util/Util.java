package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static Connection cachedConnection;
    private static SessionFactory sessionFactory;
    private static final String MYSQL_URL_PREFIX = "jdbc:mysql://";
    private static final String MYSQL_PORT_STRING = ":3306/";
    private static final String HOST_NAME = "localhost";
    private static final String DB_NAME = "kata_db";
    private static final String USER_NAME = "katauser";
    private static final String PASSWORD = "katauser";

    private static final String HB_PROP_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private static final String HB_PROP_DIALECT = "org.hibernate.dialect.MySQL8Dialect";
    private static final String HB_PROP_CURRENT_SESSION_CONTEXT_CLASS = "thread";
    private static final String HB_PROP_HBM2DDL_AUTO = "none";
    private static final String HB_PROP_SHOW_SQL = "false";


    public static Connection getConnection() throws SQLException {
        return getConnection(HOST_NAME, DB_NAME, USER_NAME, PASSWORD);
    }

    public static Connection getConnection(
            String hostName, String dbName, String userName, String password) throws SQLException {
        if (cachedConnection == null || cachedConnection.isClosed()) {
            cachedConnection = DriverManager.getConnection(
                    MYSQL_URL_PREFIX + hostName + MYSQL_PORT_STRING + dbName,
                    userName,
                    password);
        }
        return cachedConnection;
    }

    public static Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration config = new Configuration();
            config.setProperties(getConfiguredProperties());
            config.addAnnotatedClass(User.class);
            try {
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(config.getProperties())
                        .build();
                sessionFactory = config.buildSessionFactory(serviceRegistry);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return sessionFactory;
    }

    private static Properties getConfiguredProperties() {
        return getConfiguredProperties(HOST_NAME, DB_NAME, USER_NAME, PASSWORD);
    }

    private static Properties getConfiguredProperties(String hostName, String dbName, String userName, String password) {
        Properties result = new Properties();
        result.put(Environment.DRIVER, HB_PROP_DRIVER_CLASS);
        result.put(Environment.URL, MYSQL_URL_PREFIX + hostName + MYSQL_PORT_STRING + dbName);
        result.put(Environment.USER, userName);
        result.put(Environment.PASS, password);
        result.put(Environment.DIALECT, HB_PROP_DIALECT);
        result.put(Environment.SHOW_SQL, HB_PROP_SHOW_SQL);
        result.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, HB_PROP_CURRENT_SESSION_CONTEXT_CLASS);
        result.put(Environment.HBM2DDL_AUTO, HB_PROP_HBM2DDL_AUTO);
        return result;
    }
}
