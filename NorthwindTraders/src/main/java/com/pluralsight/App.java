package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;
        try {
            // Create the database connection
            // This is like opening MySQL workbench and clicking localhost
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", "root", "yearup");

            // Create statement
            // Like opening a new query window
            statement = connection.createStatement();

            // This defines the query
            // Like typing the query in the new query window
//            Scanner input = new Scanner(System.in);
//            System.out.println("Please enter your query into the Northwind database: ");
//            String query = input.nextLine().trim();
            String query = "SELECT productName FROM products";

            // Executes the query
            // This is like clicking the lightning bolt
            results = statement.executeQuery(query);

            // Process the results
            // This is a way to view the result set but java doesn't have a spreadsheet view for us
            while (results.next()) {
                String product = results.getString("productName");
                System.out.println(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Close the connection
            // Closing mysql workbench
            if (results != null) try { results.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//package com.pluralsight;
//
//import java.sql.*;
//
//public class App {
//
//    public static void main(String[] args) {
//
//        try {
//
//            // 1. open a connection to the database
//            // use the database URL to point to the correct database
//
//            //this is like opening MySQL workbench and clicking localhost
//            Connection connection;
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", "root", "yearup");
//
//
//            // create statement
//            // the statement is tied to the open connection
//
//            //like me opening a new query window
//            Statement statement = connection.createStatement();
//
//            // define your query
//
//            //like me typing the query in the new query windows
//            String query = "SELECT Title FROM Film";
//
//            // 2. Execute your query
//
//            //this is like me clicking the lightning bolt
//            ResultSet results = statement.executeQuery(query);
//
//            // process the results
//            //this is a way to view the result set but java doesnt have a spreadsheet view for us
//            while (results.next()) {
//                String title = results.getString("Title");
//                System.out.println(title);
//            }
//
//            // 3. Close the connection
//
//            //closing mysql workbench
//            connection.close();
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            //closing mysql workbench
//            connection.close();
//        }
//
//
//    }
//
//
//}