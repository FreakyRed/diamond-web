package sk.tuke.gamestudio;

import sk.tuke.gamestudio.game.diamond.frajkor.core.Field;
import sk.tuke.gamestudio.game.diamond.frajkor.userinterface.ConsoleUI;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.comment.CommentServiceJPA;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.rating.RatingServiceJPA;
import sk.tuke.gamestudio.service.score.ScoreService;
import sk.tuke.gamestudio.service.score.ScoreServiceJPA;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class SpringClient {

    public static void main(String[] args) {
        SpringApplication.run(SpringClient.class,args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI ui){
        return args -> ui.run();
    }

    @Bean
    public ConsoleUI diamondFrajkorConsoleUI(Field field){
        return new ConsoleUI(field);
    }

    @Bean
    public Field diamondFrajkorField(){
        return new Field();
    }

    @Bean
    public RatingService ratingService(){
        return new RatingServiceJPA();
    }

    @Bean
    public CommentService commentService(){
        return new CommentServiceJPA();
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }
}
