package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RatingRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public Rating getUsersRatingFromRecipe(String userId, String recipeId) {
        return ratingRepository.findByUser_IdAndRecipe_Id(userId, recipeId)
                .orElse(Rating.builder()
                        .user(ChefUser.ofId(userId))
                        .value(0)
                        .recipe(Recipe.ofId(recipeId))
                        .build());
    }
}
