package com.github.flooooooooooorian.meinkochbuch.dtos;

import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.models.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDto {

    private Long id;
    private String name;
    private Instant createdAt;
    private ChefUserPreviewDto owner;
    private String instruction;
    private int duration;
    private Difficulty difficulty;
    private int portions;
    private Image thumbnail;
    private List<Image> images;
    private List<Ingredient> ingredients;
    private boolean privacy;
    private BigDecimal ratingAverage;
    private int ratingCount;
    private List<Tag> tags;
}
