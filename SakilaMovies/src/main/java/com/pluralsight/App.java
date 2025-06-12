package com.pluralsight;

import com.pluralsight.dao.ActorDao;
import com.pluralsight.dao.FilmDao;
import com.pluralsight.models.Actor;
import com.pluralsight.models.Film;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import java.util.List;
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

        // Create the datasource
        BasicDataSource dataSource = new BasicDataSource();
        // Configure the datasource
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // Create DAO (data access object) objects to interact with the database
        ActorDao dataManagerActor = new ActorDao(dataSource);
        FilmDao dataManagerFilm = new FilmDao(dataSource);

        try (dataSource) {
            while (true) {
                System.out.print("""
                        \nWhat would you like to do?
                            1) Look up actors by last name
                            2) Look up films by actor ID
                            0) Exit
                        """);
                System.out.print("Enter choice: ");
                int choice = Integer.parseInt(input.nextLine().trim());

                switch (choice) {
                    case 1 -> {
                        // This calls for the actorList() method to prompt the last name
                        String lastName = actorList();
                        // This calls the displayActorsByLastName method in the actorDao class
                        // which executes the sql statement using a connection from the BasicDataSource.
                        // Then returns a list of the actor objects
                        List<Actor> actors = dataManagerActor.displayActorsByLastName(lastName);
                        System.out.println("\nActor ID   First Name     Last Name");
                        System.out.println("--------  ------------  -------------");
                        for (Actor actor : actors) {
                            System.out.printf("%-10d %-14s %-13s\n",
                                    actor.getActorId(), actor.getFirstName(), actor.getLastName());
                        }
                    }
                    case 2 -> {
                        int actorId = filmList();
                        List<Film> films = dataManagerFilm.displayFilmsByActorId(actorId);
                        System.out.println("\nFilm ID            Title            Year  Length");
                        System.out.println("-------  -------------------------  ----  ------");
                        for (Film film : films) {
                            System.out.printf("%-9d %-25s %-6s %-6s\n",
                                    film.getFilmId(), film.getTitle(), film.getReleaseYear(), film.getLength());
                        }
                    }
                    case 0 -> {
                        System.out.println("\nGoodbye!");
                        return;
                    }
                    default -> System.out.println("\nInvalid input! Please try again.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("\nDatabase error: " + e.getMessage(), e);
        }
    }

    public static String actorList() {
        System.out.println("\nTo display a list of");
        System.out.print("actors, enter a last name: ");
        return input.nextLine().toUpperCase().trim();
    }

    public static int filmList() {
        System.out.println("\nTo view a list of movies,");
        System.out.print("enter the ID of an actor: ");
        return Integer.parseInt(input.nextLine().trim());
    }
}

//    public static void displayActorsByLastName(BasicDataSource dataSource, String lastName) {
//        try (
//                Connection connection = dataSource.getConnection();
//                PreparedStatement preparedStatement = connection.prepareStatement("""
//                        SELECT
//                            actor_Id
//                            , first_name
//                            , last_name
//                        FROM
//                            actor
//                        WHERE
//                            last_name = ?""")
//        ) {
//            preparedStatement.setString(1, lastName);
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                System.out.println("\nID   First       Last    ");
//                System.out.println("--- ----------  ------------");
//                boolean found = false;
//                while (resultSet.next()) {
//                    String first = resultSet.getString("first_name");
//                    String last = resultSet.getString("last_name");
//                    int actorId = resultSet.getInt("actor_id");
//                    System.out.printf("%-5d %-11s %-10s\n", actorId, first, last);
//                    found = true;
//                }
//                if (!found) {
//                    System.out.println("\nNo matches!");
//                }
//            }
//        } catch (SQLException e) {
//            System.out.println("\nError retrieving actors: " + e.getMessage());
//        }
//    }
//    public static void displayFilmsByActorID(BasicDataSource dataSource, int actorId) {
//        try (
//                Connection connection = dataSource.getConnection();
//                PreparedStatement preparedStatement = connection.prepareStatement("""
//                        SELECT
//                            F.title
//                        FROM
//                            film F
//                        JOIN
//                            film_actor FA ON (F.film_id = FA.film_id)
//                        JOIN
//                            actor A ON (FA.actor_id = A.actor_id)
//                        WHERE
//                            A.actor_id = ?""")
//        ) {
//            preparedStatement.setInt(1, actorId);
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                System.out.println("\nFilms for actor ID: " + actorId);
//                System.out.println("---------------------------------");
//                boolean found = false;
//                while (resultSet.next()) {
//                    String title = resultSet.getString("title");
//                    System.out.printf("%s\n", title);
//                    found = true;
//                }
//                if (!found) {
//                    System.out.println("\nNo matches!");
//                }
//            }
//        } catch (SQLException e) {
//            System.out.println("\nError retrieving movies: " + e.getMessage());
//        }
//    }
//}