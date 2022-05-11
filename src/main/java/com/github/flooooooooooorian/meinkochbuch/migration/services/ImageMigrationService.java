package com.github.flooooooooooorian.meinkochbuch.migration.services;

import com.github.flooooooooooorian.meinkochbuch.migration.models.Bild;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageMigrationService {

    private final RecipeRepository recipeRepository;

    public int migrateImagesToRecipes(List<Bild> bilder) {
        int successfullyCount = 0;
        List<String> recipeIds = bilder.stream()
                .map(Bild::getRezept_id)
                .map(String::valueOf)
                .distinct()
                .toList();

        for (String recipeId : recipeIds) {
            List<Bild> bilderOfRecipes = bilder.stream()
                    .filter(bild -> String.valueOf(bild.getRezept_id()).equals(recipeId))
                    .toList();

            if (migrateImagesToRecipe(recipeId, bilderOfRecipes)) {
                successfullyCount++;
            }
        }


        return successfullyCount;
    }

    private boolean migrateImagesToRecipe(String recipeId, List<Bild> bilder) {

        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (optionalRecipe.isEmpty()) {
            log.error("MIGRATION Recipe " + recipeId + " does not exist!");
            return false;
        }

        Recipe recipe = optionalRecipe.get();

        recipe.setImages(bilder.stream()
                .map(bild -> Image.builder()
                        .id(String.valueOf(bild.getId()))
                        .url(bild.getImage())
                        .owner(bild.getOwner_id() != 0 ? ChefUser.ofId(String.valueOf(bild.getOwner_id())) : null)
                        .build())
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
