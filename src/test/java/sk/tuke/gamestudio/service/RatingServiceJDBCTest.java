package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.rating.RatingException;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.rating.RatingServiceJDBC;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class RatingServiceJDBCTest {

    @Test
    public void getRating() throws RatingException {
        RatingService ratingService = new RatingServiceJDBC();
        Rating rating = new Rating("Martin","Random",3,new Timestamp(new Date().getTime()));

        ratingService.setRating(rating);
        int ratingActual = ratingService.getRating("Random","Martin");

        assertEquals(rating.getRating(),ratingActual);
    }

    @Test
    public void getAverageRating() throws RatingException{
        RatingService ratingService = new RatingServiceJDBC();
        Rating rating = new Rating("Martin","AvgTest",3,new Timestamp(new Date().getTime()));
        Rating rating2 = new Rating("Milan","AvgTest",5,new Timestamp(new Date().getTime()));

        ratingService.setRating(rating);
        ratingService.setRating(rating2);

        assertEquals(4,ratingService.getAverageRating("AvgTest"));
    }

    @Test
    public void setRating() throws RatingException{
        RatingService ratingService = new RatingServiceJDBC();
        Rating rating = new Rating("Martin","SetTest",3,new Timestamp(new Date().getTime()));
        Rating rating2 = new Rating("Milan","SetTest",5,new Timestamp(new Date().getTime()));

        ratingService.setRating(rating);
        ratingService.setRating(rating2);

        assertEquals(3,ratingService.getRating("SetTest","Martin"));
        assertEquals(5,ratingService.getRating("SetTest","Milan"));

        Rating ratingNew = new Rating("Martin","SetTest",4, new Timestamp(new Date().getTime()));
        ratingService.setRating(ratingNew);

        assertEquals(4,ratingService.getRating("SetTest","Martin"));
    }

    @Test
    public void testWrongRatingRange(){
        RatingService ratingService = new RatingServiceJDBC();
        Rating rating = new Rating("Martin","WrongTest",6,new Timestamp(new Date().getTime()));

        assertThrows(RatingException.class,() -> {ratingService.setRating(rating);});
    }


    @Test
    public void testRatingExceptionThrownWhenNullIsPassed(){
        RatingService ratingService = new RatingServiceJDBC();
        Rating rating = new Rating("testNull",null, 6000, new Timestamp(new Date().getTime()));
        assertThrows(RatingException.class,() -> ratingService.setRating(rating));
    }
}
