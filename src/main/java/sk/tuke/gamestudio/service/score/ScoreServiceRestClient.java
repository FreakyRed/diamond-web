package sk.tuke.gamestudio.service.score;

import sk.tuke.gamestudio.entity.Score;

import java.util.List;

public class ScoreServiceRestClient implements ScoreService {
    private static final String URL = "http://localhost:8080/api/score";

    @Override
    public void addScore(Score score) throws ScoreException {

    }

    @Override
    public List<Score> getBestScores(String game) throws ScoreException {
        return null;
    }
}
