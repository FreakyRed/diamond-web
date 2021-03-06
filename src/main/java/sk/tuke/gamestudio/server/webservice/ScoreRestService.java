package sk.tuke.gamestudio.server.webservice;

import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.score.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/score")
public class ScoreRestService {

    @Autowired
    private ScoreService scoreService;

    @PostMapping
    public void addScore(@RequestBody Score score){
        scoreService.addScore(score);
    }

    @GetMapping("/{game}")
    public List<Score> getBestScores(@PathVariable String game){
        return scoreService.getBestScores(game);
    }
}
