package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.RatingFailedException;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RatingRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RecipeService recipeService;

    public Rating getUsersRatingFromRecipe(String userId, String recipeId) {
        return ratingRepository.findByUser_IdAndRecipe_Id(userId, recipeId)
                .orElse(Rating.builder()
                        .user(ChefUser.ofId(userId))
                        .value(0)
                        .recipe(Recipe.ofId(recipeId))
                        .build());
    }

    @Transactional
    public Rating addRating(String userId, String recipeId, double rating) {
        try {
            Rating newRating = ratingRepository.save(Rating.builder()
                    .recipe(Recipe.ofId(recipeId))
                    .user(ChefUser.ofId(userId))
                    .value(rating)
                    .build());
            recipeService.recalculateRecipe(recipeId);
            return newRating;
        } catch (JpaObjectRetrievalFailureException exception) {
            throw new RatingFailedException("Rating failed!", exception);
        }
    }
}
