package sk.tuke.gamestudio.service.score;

import sk.tuke.gamestudio.entity.Score;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score){
        entityManager.persist(score);
    }

    @Override
    public List<Score> getBestScores(String game){
        return entityManager.createNamedQuery("Score.getBestScores").
                setParameter("game", game).setMaxResults(10).getResultList();
    }
}
