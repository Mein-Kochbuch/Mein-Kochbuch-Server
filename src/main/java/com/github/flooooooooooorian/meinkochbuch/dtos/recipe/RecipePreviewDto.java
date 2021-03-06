package com.github.flooooooooooorian.meinkochbuch.dtos.recipe;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipePreviewDto {

    private String id;
    private ChefUserPreviewDto owner;

    private String name;
    private ImageDto thumbnail;
    private double ratingAverage;
    private int ratingCount;

}
