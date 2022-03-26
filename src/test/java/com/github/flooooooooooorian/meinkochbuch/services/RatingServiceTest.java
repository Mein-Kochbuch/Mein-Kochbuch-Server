package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RatingRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RatingServiceTest {

    private RatingRepository ratingRepository = mock(RatingRepository.class);

    private RatingService ratingService = new RatingService(ratingRepository);

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
}