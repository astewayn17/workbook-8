package com.pluralsight.dao;

import com.pluralsight.models.Film;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDao {

    private BasicDataSource dataSource;

    public FilmDao(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Film> displayFilmsByActorId(int actorId) {

        List<Film> films = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT 
                            F.film_id
                            , F.title
                            , F.description
                            , F.release_year
                            , F.length
                        FROM 
                            film F
                        JOIN 
                            film_actor FA ON (F.film_id = FA.film_id)
                        JOIN 
                            actor A ON (FA.actor_id = A.actor_id)
                        WHERE 
                            A.actor_id = ?""")
        ) {
            preparedStatement.setInt(1, actorId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                boolean found = false;
                while (resultSet.next()) {
                    int id = resultSet.getInt("film_id");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    int releaseYear = resultSet.getInt("release_year");
                    int length = resultSet.getInt("length");
                    Film film = new Film(id, title, description, releaseYear, length);
                    films.add(film);
                    found = true;
                }
                if (!found) {
                    System.out.println("\nNo matches!");
                }
            }
        } catch (SQLException e) {
            System.out.println("\nError retrieving movies: " + e.getMessage());
        }
        return films;
    }
}