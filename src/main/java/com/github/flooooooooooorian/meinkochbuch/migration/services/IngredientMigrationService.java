package com.github.flooooooooooorian.meinkochbuch.migration.services;

import com.github.flooooooooooorian.meinkochbuch.migration.models.Zutat;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.BaseIngredient;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.repository.IngredientRepository;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class IngredientMigrationService {
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final IdUtils idUtils;

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
                .id(idUtils.generateId())
                .migrationId(zutat.getId())
                .baseIngredient(zutat.getGlobalZutat() != null ? BaseIngredient.ofId(String.valueOf(zutat.getGlobalZutat().getId())) : null)
                .amount(zutat.getMenge())
                .text(zutat.getZutat())
                .build();

        if (ingredientRepository.existsByMigrationId(zutat.getId())) {
            log.warn("MIGRATION Ingredient already exists!");
            return false;
        }

        try {
            ingredientRepository.save(ingredient);
        } catch (IllegalArgumentException ex) {
            log.error("MIGRATION", ex);
            return false;
        }

        return true;
    }

    public int migrateIngredientsToRecipes(List<Zutat> zutats) {
        int successfullyCount = 0;
        List<Integer> recipeIds = zutats.stream()
                .map(Zutat::getRezept_id)
                .distinct()
                .toList();

        for (int recipeId : recipeIds) {
            List<Zutat> zutatsOfRecipe = zutats.stream()
                    .filter(zutat -> zutat.getRezept_id() == recipeId)
                    .toList();

            if (migrateIngredientsToRecipe(recipeId, zutatsOfRecipe)) {
                successfullyCount++;
            }
        }


        return successfullyCount;
    }

    private boolean migrateIngredientsToRecipe(int recipeId, List<Zutat> zutat) {

        Optional<Recipe> optionalRecipe = recipeRepository.findByMigrationId(recipeId);

        if (optionalRecipe.isEmpty()) {
            log.error("MIGRATION Recipe " + recipeId + " does not exist!");
            return false;
        }

        Recipe recipe = optionalRecipe.get();

        recipe.setIngredients(zutat.stream()
                .map(Zutat::getId)
                .map(migrationId -> ingredientRepository.findByMigrationId(migrationId).get())
                .collect(Collectors.toList()));

        try {
            recipeRepository.save(recipe);
        } catch (IllegalArgumentException ex) {
            log.error("MIGRATION", ex);
            return false;
        }

        return true;
    }
}
