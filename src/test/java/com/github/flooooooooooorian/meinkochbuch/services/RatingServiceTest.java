package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.RatingFailedException;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RatingRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RatingServiceTest {

    private final RatingRepository ratingRepository = mock(RatingRepository.class);

    private final RatingService ratingService = new RatingService(ratingRepository);

    @Test
    void getUsersRatingFromRecipe_whenValid_returnValidRating() {
        //GIVEN
        when(ratingRepository.findByUser_IdAndRecipe_Id("1", "1"))
                .thenReturn(Optional.ofNullable(Rating.builder()
                        .recipe(Recipe.ofId("1"))
                        .value(1)
                        .user(ChefUser.ofId("1"))
                        .build()));

        //WHEN

        Rating result = ratingService.getUsersRatingFromRecipe("1", "1");

        //THEN
        Rating expected = Rating.builder()
                .recipe(Recipe.ofId("1"))
                .value(1)
                .user(ChefUser.ofId("1"))
                .build();

        assertThat(result.getValue(), Matchers.is(expected.getValue()));
        assertThat(result.getUser().getId(), Matchers.is(expected.getUser().getId()));
        assertThat(result.getRecipe().getId(), Matchers.is(expected.getRecipe().getId()));
    }

    @Test
    void getUsersRatingFromRecipe_whenUserNotRated_returnEmptyRating() {
        //GIVEN
        when(ratingRepository.findByUser_IdAndRecipe_Id("1", "1"))
                .thenReturn(Optional.empty());

        //WHEN

        Rating result = ratingService.getUsersRatingFromRecipe("1", "1");

        //THEN
        Rating expected = Rating.builder()
                .recipe(Recipe.ofId("1"))
                .value(0)
                .user(ChefUser.ofId("1"))
                .build();

        assertThat(result.getValue(), Matchers.is(expected.getValue()));
        assertThat(result.getUser().getId(), Matchers.is(expected.getUser().getId()));
        assertThat(result.getRecipe().getId(), Matchers.is(expected.getRecipe().getId()));
    }

    @Test
    void addRating_newRating() {
        //GIVEN
        Rating rating = Rating.builder()
                .value(3.5)
                .user(ChefUser.ofId("1"))
                .recipe(Recipe.ofId("1"))
                .build();

        when(ratingRepository.save(any()))
                .thenReturn(rating);

        //WHEN

        Rating result = ratingService.addRating("1", "1", 3.5);

        //THEN

        assertThat(result.getValue(), Matchers.is(rating.getValue()));
        assertThat(result.getUser().getId(), Matchers.is(rating.getUser().getId()));
        assertThat(result.getRecipe().getId(), Matchers.is(rating.getRecipe().getId()));
    }

    @Test
    void addRating_changeRating() {
        //GIVEN
        Rating rating = Rating.builder()
                .value(3.5)
                .user(ChefUser.ofId("1"))
                .recipe(Recipe.ofId("1"))
                .build();

        when(ratingRepository.save(any()))
                .thenReturn(rating);

        //WHEN

        Rating result = ratingService.addRating("1", "1", 3.5);

        //THEN

        assertThat(result.getValue(), Matchers.is(rating.getValue()));
        assertThat(result.getUser().getId(), Matchers.is(rating.getUser().getId()));
        assertThat(result.getRecipe().getId(), Matchers.is(rating.getRecipe().getId()));
    }

    @Test
    void addRating_RecipeDoesNotExcists() {
        //GIVEN
        when(ratingRepository.save(any()))
                .thenThrow(new JpaObjectRetrievalFailureException(new EntityNotFoundException()));

        //WHEN
        //THEN

        assertThrows(RatingFailedException.class, () -> ratingService.addRating("1", "1", 3.5));
    }

    @Test
    void addRating_UserDoesNotExcists() {
        //GIVEN
        when(ratingRepository.save(any()))
                .thenThrow(new JpaObjectRetrievalFailureException(new EntityNotFoundException()));

        //WHEN
        //THEN

        assertThrows(RatingFailedException.class, () -> ratingService.addRating("1", "1", 3.5));
    }
}