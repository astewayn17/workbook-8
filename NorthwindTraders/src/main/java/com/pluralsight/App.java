package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // Making sure we passed in 2 arguments from the command line when we run the app
        // This is done with the app configuration in intellij (page 45 of the wb)
        if(args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " + "java com.pluralsight.App <username> <password>"
            );
            System.exit(1);
        }

        // Get the username and password from the command line args
        String username = args[0];
        String password = args[1];

        // Create the datasource
        BasicDataSource dataSource = new BasicDataSource();
        // Configure the datasource
        dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // Asking user for their input
        Scanner input = new Scanner(System.in);
        System.out.print("""
                What do you want to do?
                  1) Display all products
                  2) Display all customers
                  3) Display all categories
                  0) Exit
                """);
        System.out.print("Select an option: ");
        int choice = input.nextInt();

        try (dataSource) {

            if (choice == 1) {
                try (
                        // Create the connection
                        Connection connection = dataSource.getConnection();
                        // Start the preparedStatement
                        // Like opening a new query window
                        PreparedStatement preparedStatement = connection.prepareStatement(
                                "SELECT productID, productName, unitPrice, unitsInStock FROM products"
                        );
                        // Executes the query
                        // This is like clicking the lightning bolt
                        ResultSet resultSet = preparedStatement.executeQuery()
                ) {
                    System.out.println("\n ID                Name                Price   Stock");
                    System.out.println("---- -------------------------------- ------- -------");
                    while (resultSet.next()) {
                        String productId = resultSet.getString("productId");
                        String productName = resultSet.getString("productName");
                        double unitPrice = resultSet.getDouble("unitPrice");
                        String unitsInStock = resultSet.getString("unitsInStock");
                        System.out.printf("%-4s %-32s $%5.2f %6s\n", productId, productName, unitPrice, unitsInStock);
                    }
                }
            } else if (choice == 2) {
                try (
                        Connection connection = dataSource.getConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement(
                                "SELECT ContactName, CompanyName, City, Country, Phone FROM customers ORDER BY country"
                        );
                        ResultSet resultSet = preparedStatement.executeQuery()
                ) {
                    System.out.println("\nContact Name             Company Name                          City             Country       Phone");
                    System.out.println("------------             ------------                          ----             -------       -----");
                    while (resultSet.next()) {
                        String contactName = resultSet.getString("ContactName");
                        String companyName = resultSet.getString("CompanyName");
                        String city = resultSet.getString("City");
                        String country = resultSet.getString("Country");
                        String phone = resultSet.getString("Phone");
                        System.out.printf("%-24s %-37s %-16s %-13s %-15s%n", contactName, companyName, city, country, phone);
                    }
                }
            } else if (choice == 3) {
                try (
                        Connection connection = dataSource.getConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement(
                                "SELECT CategoryID, CategoryName FROM categories"
                        );
                        ResultSet resultSet = preparedStatement.executeQuery()
                ) {
                    System.out.println("\n ID      Name    ");
                    System.out.println("---- ------------");
                    while (resultSet.next()) {
                        String categoryId = resultSet.getString("CategoryID");
                        String categoryName = resultSet.getString("CategoryName");
                        System.out.printf("%-5s %-15s%n", categoryId, categoryName);
                    }
                }

                System.out.print("\nEnter a Category ID to display all products relating to it: ");
                int catId = input.nextInt();
                try (
                        Connection connection = dataSource.getConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement(
                                "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products WHERE CategoryID = ?"
                        )
                ) {
                    preparedStatement.setInt(1, catId);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        System.out.println("\nProduct ID                Name                Price   Stock");
                        System.out.println("----------- -------------------------------- -------- ------");
                        while (resultSet.next()) {
                            String productId = resultSet.getString("ProductID");
                            String productName = resultSet.getString("ProductName");
                            double unitPrice = resultSet.getDouble("UnitPrice");
                            String unitsInStock = resultSet.getString("UnitsInStock");
                            System.out.printf("%-12s %-32s $%5.2f %6s\n", productId, productName, unitPrice, unitsInStock);
                        }
                    }
                }
            } else if (choice == 0) {
                System.out.println("\nGoodbye!");
            } else {
                System.out.println("\nInvalid input! Goodbye.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
}
