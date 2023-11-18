package com.bookstore.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            throw new RuntimeException(e);
        }
    }


}
