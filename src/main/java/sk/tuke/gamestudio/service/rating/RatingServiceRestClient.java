package sk.tuke.gamestudio.service.rating;

import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;

public class RatingServiceRestClient implements RatingService {
    private static final String URL = "http://localhost:8080/api/rating";

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(URL, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) {
        try {
            return restTemplate.getForEntity(URL + "/" + game, int.class).getBody();
        } catch (HttpServerErrorException e){
            throw new RatingException("Error getting average rating",e);
        }
    }

    @Override
    public int getRating(String game, String player) {
        try {
            return restTemplate.getForEntity(URL + "/" + game + "/" + player, int.class).getBody();
        }catch (HttpServerErrorException e){
            throw new RatingException("Error getting rating",e);
        }
    }
}
