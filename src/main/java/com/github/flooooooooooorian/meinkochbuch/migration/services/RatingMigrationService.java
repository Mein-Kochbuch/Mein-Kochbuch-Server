package com.github.flooooooooooorian.meinkochbuch.migration.services;

import com.github.flooooooooooorian.meinkochbuch.migration.models.Bewertung;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
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

    public int migrateRatingsToRecipes(List<Bewertung> bewertungen) {
        int successfullyCount = 0;
        List<String> recipeIds = bewertungen.stream()
                .map(Bewertung::getRezept_id)
                .map(String::valueOf)
                .distinct()
                .toList();

        for (String recipeId : recipeIds) {
            List<Bewertung> bewertungenOfRecipe = bewertungen.stream()
                    .filter(bewertung -> String.valueOf(bewertung.getRezept_id()).equals(recipeId))
                    .toList();

            if (migrateRatingsToRecipe(recipeId, bewertungenOfRecipe)) {
                successfullyCount++;
            }
        }
        return successfullyCount;
    }

    @Transactional
    public boolean migrateRatingsToRecipe(String recipeId, List<Bewertung> bewertungen) {

        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (optionalRecipe.isEmpty()) {
            log.error("MIGRATION Recipe " + recipeId + " does not exist!");
            return false;
        }

        Recipe recipe = optionalRecipe.get();

        recipe.setRatings(bewertungen.stream()
                .map(bewertung -> Rating.builder()
                        .value(bewertung.getRating())
                        .user(ChefUser.ofId(String.valueOf(bewertung.getUser_id())))
                        .recipe(Recipe.ofId(recipeId))
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
