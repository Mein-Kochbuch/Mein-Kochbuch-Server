package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeCreationDto;
import com.github.flooooooooooorian.meinkochbuch.exceptions.RecipePrivacyForbiddenException;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.repository.IngredientRepository;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {


    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IdUtils idUtils;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void getAllRecipesWithOutUser() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .cookbooks(List.of())
                .credentialsNonExpired(true)
                .favoriteRecipes(List.of())
                .enabled(true)
                .name("my-name")
                .recipes(List.of())
                .build();

        Recipe r1 = Recipe.builder()
                .id("1")
                .owner(chefUser1)
                .privacy(false)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        Recipe r2 = Recipe.builder()
                .id("2")
                .owner(chefUser1)
                .privacy(true)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        when(recipeRepository.findAllByPrivacyIsFalseOrOwner_Id(null)).thenReturn(List.of(r1));

        //WHEN

        List<Recipe> result = recipeService.getAllRecipes(Optional.empty());

        //THEN
        assertThat(result, Matchers.containsInAnyOrder(r1));
    }

    @Test
    void getAllRecipesAndOwnRecipes() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .cookbooks(List.of())
                .credentialsNonExpired(true)
                .favoriteRecipes(List.of())
                .enabled(true)
                .name("my-name")
                .recipes(List.of())
                .build();

        Recipe r1 = Recipe.builder()
                .id("1")
                .owner(chefUser1)
                .privacy(false)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        Recipe r2 = Recipe.builder()
                .id("2")
                .owner(chefUser1)
                .privacy(true)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        when(recipeRepository.findAllByPrivacyIsFalseOrOwner_Id("1")).thenReturn(List.of(r1, r2));

        //WHEN

        List<Recipe> result = recipeService.getAllRecipes(Optional.of(chefUser1));

        //THEN
        assertThat(result, Matchers.containsInAnyOrder(r1, r2));
    }

    @Test
    void getRecipeById() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .cookbooks(List.of())
                .credentialsNonExpired(true)
                .favoriteRecipes(List.of())
                .enabled(true)
                .name("my-name")
                .recipes(List.of())
                .build();

        Recipe r1 = Recipe.builder()
                .id("1")
                .owner(chefUser1)
                .privacy(false)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        when(recipeRepository.findById("1")).thenReturn(Optional.ofNullable(r1));

        //WHEN

        Recipe result = recipeService.getRecipeById("1", Optional.empty());

        //THEN

        assertThat(result, Matchers.is(r1));
    }

    @Test
    void getRecipeByIdAnonymousDenied() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .cookbooks(List.of())
                .credentialsNonExpired(true)
                .favoriteRecipes(List.of())
                .enabled(true)
                .name("my-name")
                .recipes(List.of())
                .build();

        Recipe r1 = Recipe.builder()
                .id("1")
                .owner(chefUser1)
                .privacy(true)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        when(recipeRepository.findById("1")).thenReturn(Optional.ofNullable(r1));

        //WHEN
        //THEN

        assertThrows(RecipePrivacyForbiddenException.class, () -> recipeService.getRecipeById("1", Optional.empty()));
    }

    @Test
    void getRecipeByIdUserAllowed() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .cookbooks(List.of())
                .credentialsNonExpired(true)
                .favoriteRecipes(List.of())
                .enabled(true)
                .name("my-name")
                .recipes(List.of())
                .build();

        Recipe r1 = Recipe.builder()
                .id("1")
                .owner(chefUser1)
                .privacy(true)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        when(recipeRepository.findById("1")).thenReturn(Optional.ofNullable(r1));

        //WHEN

        Recipe result = recipeService.getRecipeById("1", Optional.of(chefUser1));

        //THEN

        assertThat(result, Matchers.is(r1));
    }

    @Test
    void addRecipe() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .cookbooks(List.of())
                .credentialsNonExpired(true)
                .favoriteRecipes(List.of())
                .enabled(true)
                .name("my-name")
                .recipes(List.of())
                .build();

        Ingredient ingredient = Ingredient.builder()
                .id("1")
                .text("test-ingredient")
                .amount(BigDecimal.valueOf(20))
                .build();

        RecipeCreationDto creationDto = RecipeCreationDto.builder()
                .privacy(false)
                .name("test-name")
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .ingredients(List.of(ingredient))
                .build();

        Recipe r1Result = Recipe.builder()
                .id("1")
                .owner(chefUser1)
                .name("test-name")
                .privacy(false)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        when(recipeRepository.save(any())).thenReturn(r1Result);
        when(ingredientRepository.save(any())).thenReturn(ingredient);
        when(idUtils.generateId()).thenReturn("uuid-1");

        //WHEN

        Recipe result = recipeService.addRecipe(creationDto, chefUser1.getId());

        //THEN

        assertThat(result, Matchers.is(r1Result));
    }
}
