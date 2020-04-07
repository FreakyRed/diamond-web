package gamestudio;

import gamestudio.game.diamond.frajkor.core.Field;
import gamestudio.game.diamond.frajkor.userinterface.ConsoleUI;
import gamestudio.server.service.score.ScoreService;
import gamestudio.server.service.score.ScoreServiceJPA;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
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
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }
}
