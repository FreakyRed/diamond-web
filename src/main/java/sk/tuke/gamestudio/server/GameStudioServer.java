package sk.tuke.gamestudio.server;

import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.game.diamond.frajkor.userinterface.Bot;
import sk.tuke.gamestudio.game.diamond.frajkor.userinterface.EasyBot;
import sk.tuke.gamestudio.server.webservice.ScoreRestService;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.comment.CommentServiceJPA;
import sk.tuke.gamestudio.service.comment.CommentServiceRestClient;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.rating.RatingServiceJPA;
import sk.tuke.gamestudio.service.rating.RatingServiceRestClient;
import sk.tuke.gamestudio.service.score.ScoreService;
import sk.tuke.gamestudio.service.score.ScoreServiceJPA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.service.score.ScoreServiceRestClient;
import sk.tuke.gamestudio.service.user.UserService;
import sk.tuke.gamestudio.service.user.UserServiceJPA;

@SpringBootApplication
@Configuration
@EntityScan({"sk.tuke.gamestudio.entity"})
public class GameStudioServer {

    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean(name = "scoreServiceServer")
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean(name="commentServiceServer")
    public CommentService commentService() {
        return new CommentServiceJPA();
    }

    @Bean(name="ratingServiceServer")
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }

    @Bean(name="userServiceServer")
    public UserService userService() {return new UserServiceJPA();}

}
