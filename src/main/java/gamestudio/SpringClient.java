package gamestudio;

import gamestudio.game.diamond.frajkor.core.Field;
import gamestudio.game.diamond.frajkor.userinterface.ConsoleUI;
import gamestudio.service.comment.CommentService;
import gamestudio.service.comment.CommentServiceJPA;
import gamestudio.service.rating.RatingService;
import gamestudio.service.rating.RatingServiceJPA;
import gamestudio.service.score.ScoreService;
import gamestudio.service.score.ScoreServiceJPA;
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
