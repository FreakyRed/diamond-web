package sk.tuke.gamestudio.service.comment;

import java.sql.*;
import sk.tuke.gamestudio.entity.Comment;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {
    public static final String URL = "jdbc:postgresql://localhost/sk.tuke.gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";

    public static final String INSERT_COMMENT =
            "INSERT INTO comment (player, game, comment, commentedon) VALUES(?, ?, ?, ?)";

    @Override
    public void addComment(Comment comment) throws CommentException {
        try(Connection connection = DriverManager.getConnection(URL,USER,PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COMMENT)) {
                preparedStatement.setString(1, comment.getPlayer());
                preparedStatement.setString(2, comment.getGame());
                preparedStatement.setString(3, comment.getComment());
                preparedStatement.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));

                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e){
            throw new CommentException("Error saving result", e);
        }
    }

    public static final String GET_COMMENTS =
            "SELECT player,game,comment,commentedon FROM comment WHERE game = ?";

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        List<Comment> commentList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(URL,USER,PASSWORD)){
            try(PreparedStatement preparedStatement = connection.prepareStatement(GET_COMMENTS)){
                preparedStatement.setString(1, game);
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while(resultSet.next()){
                        Comment comment = new Comment(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getTimestamp(4)
                        );
                        commentList.add(comment);
                    }
                }
            }
        }catch (SQLException e){
            throw new CommentException("Error loading comments",e);
        }
        return commentList;
    }

    public static void main(String[] args) throws Exception{
        Comment comment = new Comment("Freaky","Kitty","This game is great.",new java.util.Date());
        CommentService commentService = new CommentServiceJDBC();

        commentService.addComment(comment);
        System.out.println(commentService.getComments("Diamond"));
    }
}
