package com.github.flooooooooooorian.meinkochbuch.migration.services;

import com.github.flooooooooooorian.meinkochbuch.migration.models.Zutat;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.BaseIngredient;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.repository.IngredientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class IngredientMigrationService {
    private IngredientRepository ingredientRepository;

    public int migrateIngredients(List<Zutat> zutats) {
        int successfullyCount = 0;
        for (Zutat zutat : zutats) {
            if (migrateZutat(zutat)) {
                successfullyCount++;
            }
        }

        return successfullyCount;
    }

    private boolean migrateZutat(Zutat zutat) {
        Ingredient ingredient = Ingredient.builder()
                .id(String.valueOf(zutat.getId()))
                .baseIngredient(zutat.getGlobalZutat() != null ? BaseIngredient.ofId(String.valueOf(zutat.getGlobalZutat().getId())) : null)
                .amount(zutat.getMenge())
                .text(zutat.getName())
                .build();

        try {
            ingredientRepository.save(ingredient);
        } catch (IllegalArgumentException ex) {
            log.error("MIGRATION", ex);
            return false;
        }

        return true;
    }
}
