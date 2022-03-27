package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.RecipeDoesNotExistException;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavorizeService {

    private final ChefUserRepository chefUserRepository;
    private final RecipeRepository recipeRepository;

    public boolean favorizeRecipe(String userId, String recipeId) {
        ChefUser chefUser = chefUserRepository.findChefUserById(userId).orElseThrow();
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeDoesNotExistException("Recipe does not exist!"));
        List<Recipe> favorites = chefUser.getFavoriteRecipes();


        if (favorites == null) {
            chefUser.setFavoriteRecipes(List.of(recipe));
            chefUserRepository.save(chefUser);
            return true;
        }

        boolean isAlreadyFavorite = favorites.contains(recipe);

        List<Recipe> newFavorites = new ArrayList<>(favorites);
        if (isAlreadyFavorite) {
            newFavorites.remove(recipe);

            chefUser.setFavoriteRecipes(newFavorites);
            chefUserRepository.save(chefUser);
            return false;
        } else {
            newFavorites.add(recipe);

            chefUser.setFavoriteRecipes(newFavorites);
            chefUserRepository.save(chefUser);
            return true;
        }
    }
}
