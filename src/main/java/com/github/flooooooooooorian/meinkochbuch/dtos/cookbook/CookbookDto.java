package com.github.flooooooooooorian.meinkochbuch.dtos.cookbook;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookbookDto {

    private String id;
    private String name;
    private boolean privacy;
    private List<RecipePreviewDto> recipes;
    private ChefUserPreviewDto owner;
    private ImageDto thumbnail;
    private BigDecimal ratingAverage;
}
