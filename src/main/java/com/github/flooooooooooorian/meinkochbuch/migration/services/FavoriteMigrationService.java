package com.github.flooooooooooorian.meinkochbuch.migration.services;


import com.github.flooooooooooorian.meinkochbuch.migration.models.LieblingsRezept;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteMigrationService {

    private final ChefUserRepository chefUserRepository;
    private final RecipeRepository recipeRepository;

    public int migrateAllFavorites(List<LieblingsRezept> lieblingsRezepts) {
        int successfullyCount = 0;
        for (LieblingsRezept lieblingsRezept : lieblingsRezepts) {
            if (migrateFavorite(lieblingsRezept)) {
                successfullyCount++;
            }
        }

        return successfullyCount;
    }

    public boolean migrateFavorite(LieblingsRezept lieblingsRezept) {
        Optional<ChefUser> optionalChefUser = chefUserRepository.findByMigrationId(lieblingsRezept.getKochbuchuser_id());

        if (optionalChefUser.isEmpty()) {
            log.error("MIGRATION Favorite " + lieblingsRezept.getKochbuchuser_id() + " User not found!");
            return false;
        }
        ChefUser user = optionalChefUser.get();

        List<Recipe> favorites = new ArrayList<>(user.getFavoriteRecipes());

        Optional<Recipe> optionalRecipe = recipeRepository.findByMigrationId(lieblingsRezept.getRezept_id());

        if (optionalRecipe.isEmpty()) {
            log.error("MIGRATION Favorite " + lieblingsRezept.getId() + " Recipe not found!");
            return false;
        }

        favorites.add(optionalRecipe.get());

        user.setFavoriteRecipes(favorites);

        try {
            chefUserRepository.save(user);
        } catch (IllegalArgumentException ex) {
            log.error("MIGRATION", ex);
            return false;
        }

        return true;
    }
}
