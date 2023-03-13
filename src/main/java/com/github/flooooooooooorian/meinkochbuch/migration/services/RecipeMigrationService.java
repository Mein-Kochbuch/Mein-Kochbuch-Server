package com.github.flooooooooooorian.meinkochbuch.migration.services;

import com.github.flooooooooooorian.meinkochbuch.migration.models.Rezept;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class RecipeMigrationService {

    private final IdUtils idUtils;
    private final RecipeRepository recipeRepository;
    private final ChefUserRepository chefUserRepository;

    public int migrateRecipes(List<Rezept> rezeptList) {
        int successfullyCount = 0;
        for (Rezept rezept : rezeptList) {
            if (migrateRecipe(rezept)) {
                successfullyCount++;
            }
        }

        return successfullyCount;
    }

    public boolean migrateRecipe(Rezept rezept) {

        Optional<ChefUser> optionalChefUser = chefUserRepository.findByMigrationId(rezept.getOwnerId());

        if (optionalChefUser.isEmpty()) {
            log.warn("MIGRATION Recipe Owner does not exists!");
            return false;
        }

        Recipe recipe = Recipe.builder()
                .id(idUtils.generateId())
                .migrationId(rezept.getId())
                .name(rezept.getName())
                .instruction(rezept.getInstruction())
                .relevance(BigInteger.valueOf(rezept.getRelevanz()))
                .thumbnail(null)
                .averageRating(rezept.getAvgRating())
                .privacy(rezept.isPrivacy())
                .duration(rezept.getDuration())
                .difficulty(Difficulty.ofId(rezept.getDifficultyId()))
                .portions(rezept.getPortions())
                .owner(optionalChefUser.get())
                .createdAt(rezept.getDateCreated())
                .build();

        if (recipeRepository.existsByMigrationId(rezept.getId())) {
            log.warn("MIGRATION Recipe already exists!");
            return false;
        }

        try {
            recipeRepository.save(recipe);
        } catch (IllegalArgumentException ex) {
            log.error("MIGRATION", ex);
            return false;
        }

        return true;
    }
}
