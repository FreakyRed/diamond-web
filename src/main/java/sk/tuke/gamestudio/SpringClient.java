package sk.tuke.gamestudio;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import sk.tuke.gamestudio.game.diamond.frajkor.core.Field;
import sk.tuke.gamestudio.game.diamond.frajkor.userinterface.ConsoleUI;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.comment.CommentServiceJPA;
import sk.tuke.gamestudio.service.comment.CommentServiceRestClient;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.rating.RatingServiceJDBC;
import sk.tuke.gamestudio.service.rating.RatingServiceJPA;
import sk.tuke.gamestudio.service.rating.RatingServiceRestClient;
import sk.tuke.gamestudio.service.score.ScoreService;
import sk.tuke.gamestudio.service.score.ScoreServiceJPA;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.service.score.ScoreServiceRestClient;

@Configuration
@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI ui) {
        return args -> ui.run();
    }

    @Bean
    public ConsoleUI diamondFrajkorConsoleUI(Field field) {
        return new ConsoleUI(field);
    }

    @Bean
    public Field diamondFrajkorField() {
        return new Field();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }
}
