package com.github.flooooooooooorian.meinkochbuch.migration.services;

import com.github.flooooooooooorian.meinkochbuch.migration.models.Bild;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import com.github.flooooooooooorian.meinkochbuch.services.AwsS3Service;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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

    private final AwsS3Service awsS3Service;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://mein-kochbuch.org/media/")
            .codecs(codecs -> codecs
                    .defaultCodecs()
                    .maxInMemorySize(500 * 1024 * 1024))
            .build();

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
                log.error("MIGRATION Recipe unkown ecxeption: " + e.getMessage(), e);
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

        String id = idUtils.generateId();

        recipe.setImages(bilder.stream()
                .map(bild -> Image.builder()
                        .id(id)
                        .migrationId(bild.getId())
                        .key(bild.getImage())
                        .owner(bild.getOwner_id() != 0 ? chefUserRepository.findByMigrationId(bild.getOwner_id()).orElseThrow() : null)
                        .build())
                .collect(Collectors.toList()));

        for (Bild bild : bilder) {
            if (!fetchImageAndUploadToS3(bild)) {
                return false;
            }
        }

        try {
            recipeRepository.save(recipe);
        } catch (Exception ex) {
            log.error("MIGRATION " + recipe, ex);
            return false;
        }

        return true;
    }

    private boolean fetchImageAndUploadToS3(Bild bild) {

        byte[] image = webClient.get()
                .uri(bild.getImage())
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        try {
            InputStream stream = new ByteArrayInputStream(image);
            awsS3Service.uploadImage(stream, bild.getImage(), image.length);
        } catch (Exception e) {
            log.error("MIGRATION", e);
            return false;
        }
        return true;
    }
}
