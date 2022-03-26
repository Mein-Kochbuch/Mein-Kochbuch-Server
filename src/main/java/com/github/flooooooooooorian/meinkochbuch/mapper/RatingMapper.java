package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.rating.RatingDto;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;

public interface RatingMapper {

    static RatingDto ratingToRatingDto(Rating rating) {
        return RatingDto.builder()
                .rating(rating.getValue())
                .recipeId(rating.getRecipe().getId())
                .build();
    }
}
