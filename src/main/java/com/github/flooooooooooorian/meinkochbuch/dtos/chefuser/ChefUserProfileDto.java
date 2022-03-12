package com.github.flooooooooooorian.meinkochbuch.dtos.chefuser;

import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreview;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefUserProfileDto {

    private String id;
    private String name;

    private List<RecipePreviewDto> recipes;
    private List<CookbookPreview> cookbooks;
}
