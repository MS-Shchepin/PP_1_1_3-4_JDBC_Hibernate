package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users " +
                    "(id BIGINT not NULL AUTO_INCREMENT, " +
                    " name VARCHAR(20), " +
                    " lastname VARCHAR(20), " +
                    " age TINYINT, " +
                    " PRIMARY KEY ( id ))";
            statement.executeUpdate(sql);
//            System.out.println("Table created if not exists");
        } catch (SQLException ex) {
//            System.out.println("Table create failed");
            ex.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DROP TABLE IF EXISTS users";
            statement.executeUpdate(sql);
//            System.out.println("Table dropped if exists");
        } catch (SQLException ex) {
//            System.out.println("Drop table failed");
            ex.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("INSERT users(name, lastname, age)" +
                    " VALUES ('%s', '%s', '%d')", name, lastName, age);
            statement.executeUpdate(sql);
            System.out.printf("User с именем – %s добавлен в базу данных%n", name);
        } catch (SQLException ex) {
//            System.out.println("Save user failed");
            ex.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("DELETE FROM users WHERE id = %d", id);
//            System.out.println("Removed user count = " + statement.executeUpdate(sql));
        } catch (SQLException ex) {
//            System.out.println("Remove user failed");
            ex.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User nextUser = new User();
                nextUser.setId(resultSet.getLong("id"));
                nextUser.setName(resultSet.getString("name"));
                nextUser.setLastName(resultSet.getString("lastname"));
                nextUser.setAge((byte) resultSet.getInt("age"));
                result.add(nextUser);
            }
//            System.out.printf("Got %d users from db%n", result.size());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
        Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE users");
//            System.out.println("Table clean");
        } catch (SQLException ex) {
//            System.out.println("Table clean failed");
            ex.printStackTrace();
        }
    }
}
