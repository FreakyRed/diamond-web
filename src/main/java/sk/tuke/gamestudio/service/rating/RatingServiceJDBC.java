package sk.tuke.gamestudio.service.rating;

import sk.tuke.gamestudio.entity.Rating;

import java.sql.*;

public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost/sk.tuke.gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";

    private static final String IS_ENTRY_IN_TABLE =
            "SELECT player,game,rating FROM rating WHERE player = ? AND game = ?";

    private static final String UPDATE_RATING =
            "UPDATE rating SET rating = ?, ratedon = ? WHERE player = ? AND game = ?";

    private static final String INSERT_RATING =
            "INSERT INTO rating (player,game,rating,ratedon) VALUES(?, ?, ?, ?)";


    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(IS_ENTRY_IN_TABLE)) {
                preparedStatement.setString(1, rating.getPlayer());
                preparedStatement.setString(2, rating.getGame());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        updateEntryInDatabase(resultSet, connection, rating);
                    } else {
                        insertEntryIntoDatabase(connection, rating);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error adding rating", e);
        }
    }

    private void updateEntryInDatabase(ResultSet resultSet, Connection connection, Rating rating) throws SQLException {
        try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_RATING)) {
            if (resultSet.getInt(3) != rating.getRating()) {
                updateStatement.setInt(1, rating.getRating());
                updateStatement.setTimestamp(2, new Timestamp(rating.getRatedOn().getTime()));
                updateStatement.setString(3, rating.getPlayer());
                updateStatement.setString(4, rating.getGame());

                updateStatement.executeUpdate();
            }
        }
    }

    private void insertEntryIntoDatabase(Connection connection, Rating rating) throws SQLException {
        try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_RATING)) {
            insertStatement.setString(1, rating.getPlayer());
            insertStatement.setString(2, rating.getGame());
            insertStatement.setInt(3, rating.getRating());
            insertStatement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));

            insertStatement.executeUpdate();
        }
    }

    private static final String GET_AVG_RATING =
            "SELECT AVG(rating) FROM rating WHERE game = ?";

    @Override
    public int getAverageRating(String game) throws RatingException {
        int averageRating = 0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_AVG_RATING)) {
                preparedStatement.setString(1, game);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        averageRating = resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error getting average rating", e);
        }
        return averageRating;
    }


    private static final String GET_RATING =
            "SELECT rating FROM rating WHERE game = ? AND player = ?";

    @Override
    public int getRating(String game, String player) throws RatingException {
        int rating = 0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_RATING)) {
                preparedStatement.setString(1, game);
                preparedStatement.setString(2, player);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        rating = resultSet.getInt("rating");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error getting rating", e);
        }
        return rating;
    }

    public static void main(String[] args) throws Exception {
        Rating rating = new Rating("freak", "Diamond", 5, new java.util.Date());
        RatingService ratingService = new RatingServiceJDBC();

        ratingService.setRating(rating);
    }
}
