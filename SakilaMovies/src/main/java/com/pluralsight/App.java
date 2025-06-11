package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.Scanner;

public class App {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Application needs two arguments to run: java com.pluralsight.App <username> <password>");
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        try (dataSource) {
            while (true) {
                System.out.print("""
                        \nWhat would you like to do?
                            1) Look up actors by last name
                            2) Look up movies by actor name
                            0) Exit
                        """);
                System.out.print("Enter choice: ");
                int choice = Integer.parseInt(input.nextLine().trim());

                switch (choice) {
                    case 1 -> {String lastName = actorList(); displayActorsByLastName(dataSource, lastName);}
                    case 2 -> {String fullName = movieList(); displayMoviesByActorName(dataSource, fullName);}
                    case 0 -> {System.out.println("\nGoodbye!"); return;}
                    default -> System.out.println("\nInvalid input! Please try again.");
                }
            }
        } catch (SQLException e) { throw new RuntimeException("\nDatabase error: " + e.getMessage(), e); }
    }

    public static String actorList() {
        System.out.println("\nTo display a list of");
        System.out.print("actors, enter a last name: ");
        return input.nextLine().toUpperCase().trim();
    }
    public static String movieList() {
        System.out.println("\nTo view a list of movies, enter");
        System.out.print("the first and last name of an actor: ");
        return input.nextLine().toUpperCase().trim();
    }

    public static void displayActorsByLastName(BasicDataSource dataSource, String lastName) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT 
                            first_name
                            , last_name 
                        FROM 
                            actor 
                        WHERE 
                            last_name = ?""")
        ) {
            preparedStatement.setString(1, lastName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("\nFirst       Last    ");
                System.out.println("----------  ------------");
                boolean found = false;
                while (resultSet.next()) {
                    String first = resultSet.getString("first_name");
                    String last = resultSet.getString("last_name");
                    System.out.printf("%-11s %-10s\n", first, last);
                    found = true;
                }
                if (!found) {
                    System.out.println("\nNo matches!");
                }
            }
        } catch (SQLException e) {
            System.out.println("\nError retrieving actors: " + e.getMessage());
        }
    }
    public static void displayMoviesByActorName(BasicDataSource dataSource, String fullName) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT 
                            F.title
                        FROM 
                            film F
                        JOIN 
                            film_actor FA ON (F.film_id = FA.film_id)
                        JOIN 
                            actor A ON (FA.actor_id = A.actor_id)
                        WHERE 
                            CONCAT (A.first_name, ' ', A.last_name) = ?""")
        ) {
            preparedStatement.setString(1, fullName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("\nMovies starring " + fullName);
                System.out.println("---------------------------------");
                boolean found = false;
                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    System.out.printf("%s\n", title);
                    found = true;
                }
                if (!found) {
                    System.out.println("\nNo matches!");
                }
            }
        } catch (SQLException e) {
            System.out.println("\nError retrieving movies: " + e.getMessage());
        }
    }
}