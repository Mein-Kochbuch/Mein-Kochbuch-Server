package com.github.flooooooooooorian.meinkochbuch.migration.services;

import com.github.flooooooooooorian.meinkochbuch.migration.models.Bewertung;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import com.github.flooooooooooorian.meinkochbuch.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingMigrationService {

    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;
    private final ChefUserRepository chefUserRepository;

    public int migrateRatingsToRecipes(List<Bewertung> bewertungen) {
        int successfullyCount = 0;
        List<Integer> recipeIds = bewertungen.stream()
                .map(Bewertung::getRezept_id)
                .distinct()
                .toList();

        for (int recipeId : recipeIds) {
            List<Bewertung> bewertungenOfRecipe = bewertungen.stream()
                    .filter(bewertung -> bewertung.getRezept_id() == recipeId)
                    .toList();

            try {
                if (migrateRatingsToRecipe(recipeId, bewertungenOfRecipe)) {
                    successfullyCount++;
                }
            } catch (Exception e) {
                log.error("MIGRATION Ratings unkown ecxeption: " + e.getMessage(), e);
            }
        }
        return successfullyCount;
    }

    @Transactional
    public boolean migrateRatingsToRecipe(int recipeId, List<Bewertung> bewertungen) {

        Optional<Recipe> optionalRecipe = recipeRepository.findByMigrationId(recipeId);

        if (optionalRecipe.isEmpty()) {
            log.error("MIGRATION Recipe " + recipeId + " does not exist!");
            return false;
        }

        Recipe recipe = optionalRecipe.get();

        recipe.setRatings(bewertungen.stream()
                .map(bewertung -> Rating.builder()
                        .migrationId(bewertung.getId())
                        .value(bewertung.getRating())
                        .user(chefUserRepository.findByMigrationId(bewertung.getUser_id()).orElseThrow())
                        .recipe(recipe)
                        .build())
                .collect(Collectors.toList()));


        try {
            recipeRepository.save(recipe);
            recipeService.recalculateRecipe(recipe.getId());
        } catch (IllegalArgumentException ex) {
            log.error("MIGRATION", ex);
            return false;
        }

        return true;
    }
}
