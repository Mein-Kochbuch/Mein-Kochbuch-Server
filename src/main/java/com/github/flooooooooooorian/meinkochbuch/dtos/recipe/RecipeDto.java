package com.github.flooooooooooorian.meinkochbuch.dtos.recipe;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.models.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDto {

    private String id;
    private String name;
    private ChefUserPreviewDto owner;
    private String instruction;
    private int duration;
    private Difficulty difficulty;
    private int portions;
    private ImageDto thumbnail;
    private List<ImageDto> images;
    private List<Ingredient> ingredients;
    private boolean privacy;
    private double ratingAverage;
    private int ratingCount;
    private List<Tag> tags;
}
