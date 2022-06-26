package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.rating.RatingDto;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingMapper {

    public RatingDto ratingToRatingDto(Rating rating) {
        return RatingDto.builder()
                .rating(rating.getValue())
                .recipeId(rating.getRecipe().getId())
                .build();
    }
}
