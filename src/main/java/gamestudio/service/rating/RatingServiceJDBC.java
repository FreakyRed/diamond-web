package gamestudio.service.rating;

import gamestudio.entity.Rating;

import java.sql.*;

public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";

    private static final String SET_RATING =
            "INSERT INTO rating ()";



    @Override
    public void setRating(Rating rating) throws RatingException {

    }

    private static final String GET_AVG_RATING =
            "SELECT AVG(rating) AS avgAmount FROM rating WHERE game = ?";

    @Override
    public int getAverageRating(String game) throws RatingException {
        int averageRating = 0;
        try(Connection connection = DriverManager.getConnection(URL,USER,PASSWORD)){
            try(PreparedStatement preparedStatement = connection.prepareStatement(GET_AVG_RATING)){
                preparedStatement.setString(1,game);
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    averageRating = resultSet.getInt(1);
                }
            }
        }catch (SQLException e){
            throw new RatingException("Error getting average rating",e);
        }
        return averageRating;
    }


    private static final String GET_RATING =
            "SELECT rating FROM rating WHERE game = ? AND player = ?";

    @Override
    public int getRating(String game, String player) throws RatingException {
        int rating = 0;
        try (Connection connection = DriverManager.getConnection(URL,USER,PASSWORD)) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(GET_RATING)){
                preparedStatement.setString(1,game);
                preparedStatement.setString(2,player);
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                        rating = resultSet.getInt("result");
                }
            }
        }catch (SQLException e){
            throw new RatingException("Error getting rating",e);
        }
        return rating;
    }
}
