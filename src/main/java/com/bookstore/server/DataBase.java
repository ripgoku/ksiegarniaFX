package com.bookstore.server;

import java.sql.*;

public class DataBase {
    final private static String url="jdbc:mysql://localhost:3306/bookstore_project";
    final private static String user="root";
    final private static String password="root123";

    public DataBase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection with "+url+" is successful!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,user,password);
    }

    public static void registerQuery(String firstName, String lastName) throws SQLException {
        try(Connection connection = getConnection(); Statement statement = connection.createStatement()) {
                String sqlQuery = "SELECT * FROM przykladowa_tabela";
                ResultSet resultSet = statement.executeQuery(sqlQuery);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nazwa = resultSet.getString("nazwa");
                    int wartosc = resultSet.getInt("wartosc");
                    System.out.println("ID: " + id + ", Nazwa: " + nazwa + ", Wartość: " + wartosc);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
