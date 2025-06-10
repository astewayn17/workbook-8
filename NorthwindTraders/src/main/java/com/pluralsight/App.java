package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // Making sure we passed in 2 arguments from the command line when we run the app
        // This is done with the app configuration in intellij (page 45 of the wb)
        if(args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "java com.pluralsight.App <username> <password>"
            );
            System.exit(1);
        }

        // Get the username and password from the command line args
        String username = args[0];
        String password = args[1];

        // Asking user for their input
        Scanner input = new Scanner(System.in);
        System.out.print("""
                What do you want to do?
                  1) Display all products
                  2) Display all customers
                  0) Exit
                Select an option: """);
        System.out.print(" ");
        int choice = input.nextInt();

        try {
            // Create the database connection and preparedStatement
            // This is like opening MySQL workbench and clicking localhost
            connection = DriverManager.getConnection( "jdbc:mysql://localhost:3306/northwind", username, password);

            if (choice == 1) {
                // Start the preparedStatement
                // Like opening a new query window
                preparedStatement = connection.prepareStatement(
                        "SELECT productID, productName, unitPrice, unitsInStock FROM products"
                );

                // Executes the query
                // This is like clicking the lightning bolt
                resultSet = preparedStatement.executeQuery();

//            while (resultSet.next()) {
//                String productId = resultSet.getString("productId");
//                String productName = resultSet.getString("productName");
//                double unitPrice = resultSet.getDouble("unitPrice");
//                String unitsInStock = resultSet.getString("unitsInStock");
//                System.out.println("✧".repeat(29));
//                System.out.println("ID:         " + productId);
//                System.out.println("Name:       " + productName);
//                System.out.printf("Price:      $%.2f\n", unitPrice);
//                System.out.println("Stock:      " + unitsInStock);
//            }
                // Doing both styles
                System.out.println(" ID                Name                Price   Stock");
                System.out.println("---- -------------------------------- ------- -------");
                while (resultSet.next()) {
                    String productId = resultSet.getString("productId");
                    String productName = resultSet.getString("productName");
                    double unitPrice = resultSet.getDouble("unitPrice");
                    String unitsInStock = resultSet.getString("unitsInStock");
                    System.out.printf("%-4s %-32s $%5.2f %6s\n", productId, productName, unitPrice, unitsInStock);
                }
            } else if (choice == 2) {
                preparedStatement = connection.prepareStatement(
                        "SELECT ContactName, CompanyName, City, Country, Phone FROM customers ORDER BY country"
                );

                resultSet = preparedStatement.executeQuery();

                System.out.println("Contact Name             Company Name                          City             Country       Phone");
                System.out.println("------------             ------------                          ----             -------       -----");
                while (resultSet.next()) {
                    String contactName = resultSet.getString("ContactName");
                    String companyName = resultSet.getString("CompanyName");
                    String city = resultSet.getString("City");
                    String country = resultSet.getString("Country");
                    String phone = resultSet.getString("Phone");
                    System.out.printf("%-24s %-37s %-16s %-13s %-15s%n", contactName, companyName, city, country, phone);
                }

            } else if (choice == 0) {
                System.out.println("\nGoodbye!");
            } else {
                System.out.println("\nInvalid input! Goodbye.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Close the connection
            // Closing mysql workbench
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (preparedStatement != null) try { preparedStatement.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}

///=====================================================================================================================

///            // Find the question mark by index and provide its safe value
///            preparedStatement.setInt(1, 14);

///            // Process the results
///            // This is a way to view the result set but java doesn't have a spreadsheet view for us
///            while (resultSet.next()) {
///                // Process the data
///                System.out.printf(
///                        "productName = %s\n",
///                        resultSet.getString("ProductName")
///                );
///            }

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