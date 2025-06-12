package com.pluralsight.dao;

import com.pluralsight.models.Actor;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorDao {

    private BasicDataSource dataSource;

    public ActorDao(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Actor> displayActorsByLastName(String lastName) {

        List<Actor> actors = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT 
                            actor_Id
                            , first_name
                            , last_name 
                        FROM 
                            actor 
                        WHERE 
                            last_name LIKE ?""")
        ) {
            preparedStatement.setString(1, "%" + lastName + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                boolean found = false;
                while (resultSet.next()) {
                    int id = resultSet.getInt("actor_id");
                    String firstName = resultSet.getString("first_name");
                    String retrievedLastName = resultSet.getString("last_name");
                    Actor actor = new Actor(id, firstName, retrievedLastName);
                    actors.add(actor);
                    found = true;
                }
                if (!found) {
                    System.out.println("\nNo matches!");
                }
            }
        } catch (SQLException e) {
            System.out.println("\nError retrieving actors: " + e.getMessage());
        }
        return actors;
    }
}