package com.github.flooooooooooorian.meinkochbuch.migration;

import com.github.flooooooooooorian.meinkochbuch.migration.models.*;
import com.github.flooooooooooorian.meinkochbuch.migration.services.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/migration")
@AllArgsConstructor
public class MigrationController {

    private final UserMigration userMigration;
    private final BaseIngredientMigrationService baseIngredientMigrationService;
    private final IngredientMigrationService ingredientMigrationService;
    private final RecipeMigrationService recipeMigrationService;
    private final ImageMigrationService imageMigrationService;
    private final RatingMigrationService ratingMigrationService;
    private final FavoriteMigrationService favoriteMigrationService;

    @PostMapping("users")
    public MigrationResponse migrateUsers(@RequestBody List<Kochbuchuser> kochbuchuserList) {
        return MigrationResponse.builder()
                .total(kochbuchuserList.size())
                .successful(userMigration.migrateUsers(kochbuchuserList))
                .build();
    }

    @PostMapping("users/favorites")
    public MigrationResponse migrateUsersFavoriteRecipes(@RequestBody List<LieblingsRezept> lieblingsRezepts) {
        return MigrationResponse.builder()
                .total(lieblingsRezepts.size())
                .successful(favoriteMigrationService.migrateAllFavorites(lieblingsRezepts))
                .build();
    }

    @PostMapping("baseingredients")
    public MigrationResponse migrateBaseIngredients(@RequestBody List<GlobalZutat> globalZutats) {
        return MigrationResponse.builder()
                .total(globalZutats.size())
                .successful(baseIngredientMigrationService.migrateBaseIngredients(globalZutats))
                .build();
    }

    @PostMapping("ingredients")
    public MigrationResponse migrateIngredients(@RequestBody List<Zutat> zutats) {
        return MigrationResponse.builder()
                .total(zutats.size())
                .successful(ingredientMigrationService.migrateIngredients(zutats))
                .build();
    }

    @PostMapping("recipes")
    public MigrationResponse migrateRecipes(@RequestBody List<Rezept> rezepts) {
        return MigrationResponse.builder()
                .total(rezepts.size())
                .successful(recipeMigrationService.migrateRecipes(rezepts))
                .build();
    }

    @PostMapping("recipes/ingredients")
    public MigrationResponse migrateRecipesIngredients(@RequestBody List<Zutat> zutats) {
        return MigrationResponse.builder()
                .total(zutats.stream()
                        .map(Zutat::getRezept_id)
                        .map(String::valueOf)
                        .distinct()
                        .toList()
                        .size())
                .successful(ingredientMigrationService.migrateIngredientsToRecipes(zutats))
                .build();
    }

    @PostMapping("recipes/images")
    public MigrationResponse migrateRecipesImages(@RequestBody List<Bild> bilder) {
        return MigrationResponse.builder()
                .total(bilder.stream()
                        .map(Bild::getRezept_id)
                        .map(String::valueOf)
                        .distinct()
                        .toList()
                        .size())
                .successful(imageMigrationService.migrateImagesToRecipes(bilder))
                .build();
    }

    @PostMapping("recipes/ratings")
    public MigrationResponse migrateRecipesRatings(@RequestBody List<Bewertung> bewertungen) {
        return MigrationResponse.builder()
                .total(bewertungen.stream()
                        .map(Bewertung::getRezept_id)
                        .map(String::valueOf)
                        .distinct()
                        .toList()
                        .size())
                .successful(ratingMigrationService.migrateRatingsToRecipes(bewertungen))
                .build();
    }
}
