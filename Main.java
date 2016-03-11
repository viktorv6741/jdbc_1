package com.jdbc;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Main {

    private static String USER_INFO = "id = %s; name = %s; email = %s \n";
    private static String URL = "jdbc:mysql://localhost:3306/db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) throws SQLException {

        Scanner console = new Scanner(System.in);

        Driver driver = new FabricMySQLDriver();
        DriverManager.registerDriver(driver);

        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

/////////////////////////////////////////////////////////////////////////////////////////////////////////

        boolean key = true;

        while (key) {
            System.out.println("If you like to sea all users print 'SHOW'\n" +
                    "If you like to add user print 'ADD' \n" +
                    "If you like to delete user print 'DELETE'\n" +
                    "If you like to exit print 'EXIT'\n");

            String str = console.nextLine();

            switch (str) {
                case "add":
                    System.out.println("Enter the name");
                    String name = console.nextLine();

                    System.out.println("Enter the email");
                    String email = console.nextLine();

                    addUser(connection, name, email);
                    printDataAboutUsers(connection);
                    break;
                case "delete":
                    printDataAboutUsers(connection);
                    System.out.println("Enter id number");
                    int idNumber = console.nextInt();

                    deleteUser(connection, idNumber);
                    break;
                case "show":
                    printDataAboutUsers(connection);
                    System.out.println();
                    break;
                case "exit":
                    key = false;
                    break;
                default:
                    System.out.println("Not correct value, please try one more time");
            }
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void addUser(Connection connection, String name, String email) throws SQLException {

        connection.setAutoCommit(true);

        String sqlName = "INSERT INTO USER(NAME, EMAIL) VALUES (?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlName);

        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);
        preparedStatement.addBatch();
        preparedStatement.executeBatch();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void deleteUser(Connection connection, int idNumber) throws SQLException {
        connection.setAutoCommit(true);

        String sqlName = "DELETE FROM USER WHERE ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlName);

        preparedStatement.setInt(1, idNumber);

        preparedStatement.addBatch();
        preparedStatement.executeBatch();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void printDataAboutUsers(Connection connection) throws SQLException {

        Statement statement = connection.createStatement();

        statement.execute("SELECT * FROM USER");
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            System.out.printf(USER_INFO, resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////

}
