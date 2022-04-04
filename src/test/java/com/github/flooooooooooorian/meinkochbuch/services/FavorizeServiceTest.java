package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.RecipeDoesNotExistException;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FavorizeServiceTest {

    private final ChefUserRepository chefUserRepository = mock(ChefUserRepository.class);
    private final RecipeRepository recipeRepository = mock(RecipeRepository.class);
    private final FavorizeService favorizeService = new FavorizeService(chefUserRepository, recipeRepository);

    @Test
    void favorizeRecipe_firstFavorite() {
        //GIVEN
        when(chefUserRepository.findChefUserById("1"))
                .thenReturn(Optional.of(ChefUser.builder()
                        .id("1")
                        .build()));

        when(recipeRepository.findById("1")).thenReturn(Optional.of(Recipe.ofId("1")));

        //WHEN
        boolean result = favorizeService.favorizeRecipe("1", "1");

        //THEN
        assertTrue(result);
    }

    @Test
    void favorizeRecipe_newFavorite() {
        //GIVEN
        when(chefUserRepository.findChefUserById("1"))
                .thenReturn(Optional.of(ChefUser.builder()
                        .id("1")
                        .build()));

        when(recipeRepository.findById("1")).thenReturn(Optional.of(Recipe.ofId("1")));

        //WHEN
        boolean result = favorizeService.favorizeRecipe("1", "1");

        //THEN
        assertTrue(result);
    }

    @Test
    void favorizeRecipe_alreadyFavorite() {
        //GIVEN
        when(chefUserRepository.findChefUserById("1"))
                .thenReturn(Optional.of(ChefUser.builder()
                        .id("1")
                        .favoriteRecipes(List.of(Recipe.ofId("1")))
                        .build()));
        when(recipeRepository.findById("1")).thenReturn(Optional.of(Recipe.ofId("1")));

        //WHEN
        boolean result = favorizeService.favorizeRecipe("1", "1");

        //THEN
        assertFalse(result);
    }

    @Test
    void favorizeRecipe_userDoesNotExist() {
        //GIVEN
        when(chefUserRepository.findChefUserById("1"))
                .thenReturn(Optional.empty());
        when(recipeRepository.findById("1")).thenReturn(Optional.of(Recipe.ofId("1")));

        //WHEN
        //THEN

        assertThrows(NoSuchElementException.class, () -> favorizeService.favorizeRecipe("1", "1"));
    }

    @Test
    void favorizeRecipe_recipeDoesNotExist() {
        //GIVEN
        when(chefUserRepository.findChefUserById("1"))
                .thenReturn(Optional.of(ChefUser.builder()
                        .id("1")
                        .favoriteRecipes(List.of(Recipe.ofId("1")))
                        .build()));
        when(recipeRepository.findById("1")).thenReturn(Optional.empty());

        //WHEN
        //THEN

        assertThrows(RecipeDoesNotExistException.class, () -> favorizeService.favorizeRecipe("1", "1"));
    }
}