package com.github.flooooooooooorian.meinkochbuch.dtos.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDto {
    private String recipeId;

    @Min(0)
    @Max(5)
    private double rating;
}
