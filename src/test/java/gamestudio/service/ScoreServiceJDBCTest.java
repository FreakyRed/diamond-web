package gamestudio.service;

import gamestudio.entity.Score;
import gamestudio.service.score.ScoreException;
import gamestudio.service.score.ScoreService;
import gamestudio.service.score.ScoreServiceJDBC;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreServiceJDBCTest {

    @Test
    public void addScore(){
        ScoreService service = new ScoreServiceJDBC();
        Score score = new Score("Ludo","Martin",5000, new Timestamp(new Date().getTime()));
        service.addScore(score);
        List<Score> scores = service.getBestScores("Ludo");

        assertEquals(1,scores.size());
        assertEquals("Martin",scores.get(0).getPlayer());
        assertEquals(5000,scores.get(0).getPoints());
        assertEquals("Ludo",scores.get(0).getGame());
    }

    @Test
    public void getBestScores(){
        ScoreService service = new ScoreServiceJDBC();
        Score score = new Score("test","Martin",5000, new Timestamp(new Date().getTime()));
        Score score2 = new Score("test","Randy",6300, new Timestamp(new Date().getTime()));

        service.addScore(score);
        service.addScore(score2);
        List<Score> scores = service.getBestScores("test");

        assertEquals(2,scores.size());
        assertEquals("Martin",scores.get(1).getPlayer());
        assertEquals("Randy",scores.get(0).getPlayer());
    }

    @Test
    public void testScoreExceptionThrownWhenNullIsPassed(){
        ScoreService scoreService = new ScoreServiceJDBC();
        Score score = new Score("testNull",null, 6000, new Timestamp(new Date().getTime()));
        assertThrows(ScoreException.class,() -> scoreService.addScore(score));
    }
}
