package sk.tuke.gamestudio.service.rating;

import sk.tuke.gamestudio.entity.Rating;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.service.comment.CommentException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        entityManager.merge(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException{
        return ((Double) entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game).getSingleResult()).intValue();
    }

    @Override
    public int getRating(String game, String player) throws RatingException{
        return (int) entityManager.createNamedQuery("Rating.getRating")
                .setParameter("game", game)
                .setParameter("player", player)
                .getSingleResult();
    }
}
