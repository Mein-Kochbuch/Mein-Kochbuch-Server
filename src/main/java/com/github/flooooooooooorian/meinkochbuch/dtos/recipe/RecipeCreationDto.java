package com.github.flooooooooooorian.meinkochbuch.dtos.recipe;

import com.github.flooooooooooorian.meinkochbuch.dtos.ingredient.IngredientCreationDto;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeCreationDto {

    private String name;
    private String instruction;
    private int duration;
    private Difficulty difficulty;
    private int portions;
    private List<IngredientCreationDto> ingredients;
    private boolean privacy;
}
