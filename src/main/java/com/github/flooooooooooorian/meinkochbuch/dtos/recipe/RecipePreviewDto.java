package com.github.flooooooooooorian.meinkochbuch.dtos.recipe;

import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipePreviewDto {

    private Long id;
    private ChefUserPreviewDto owner;

    private String name;
    private Image thumbnail;
    private BigDecimal ratingAverage;
    private int ratingCount;

}
