package com.github.flooooooooooorian.meinkochbuch.dtos.ingredient;

import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.BaseIngredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientCreationDto {

    private String text;

    private BaseIngredient baseIngredient;
    private BigDecimal amount;
}
