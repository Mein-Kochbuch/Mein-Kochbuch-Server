package com.github.flooooooooooorian.meinkochbuch.migration.services;

import com.github.flooooooooooorian.meinkochbuch.migration.models.Bild;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
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
    private final ChefUserRepository chefUserRepository;
    private final IdUtils idUtils;

    public int migrateImagesToRecipes(List<Bild> bilder) {
        int successfullyCount = 0;
        List<Integer> recipeIds = bilder.stream()
                .map(Bild::getRezept_id)
                .distinct()
                .toList();

        for (int recipeId : recipeIds) {
            List<Bild> bilderOfRecipes = bilder.stream()
                    .filter(bild -> bild.getRezept_id() == recipeId)
                    .toList();

            try {
                if (migrateImagesToRecipe(recipeId, bilderOfRecipes)) {
                    successfullyCount++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        return successfullyCount;
    }

    private boolean migrateImagesToRecipe(int recipeId, List<Bild> bilder) {

        Optional<Recipe> optionalRecipe = recipeRepository.findByMigrationId(recipeId);

        if (optionalRecipe.isEmpty()) {
            log.error("MIGRATION Recipe " + recipeId + " does not exist!");
            return false;
        }

        Recipe recipe = optionalRecipe.get();

        recipe.setImages(bilder.stream()
                .map(bild -> Image.builder()
                        .id(idUtils.generateId())
                        .migrationId(bild.getId())
                        .url(bild.getImage())
                        .owner(bild.getOwner_id() != 0 ? chefUserRepository.findByMigrationId(bild.getOwner_id()).orElseThrow() : null)
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
