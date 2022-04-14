package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.dtos.ingredient.IngredientCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeEditDto;
import com.github.flooooooooooorian.meinkochbuch.exceptions.RecipePrivacyForbiddenException;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.repository.IngredientRepository;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import com.github.flooooooooooorian.meinkochbuch.utils.TimeUtils;
import com.github.flooooooooooorian.meinkochbuch.utils.sorting.RecipeSorting;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    private final RecipeRepository recipeRepository = mock(RecipeRepository.class);
    private final IngredientRepository ingredientRepository = mock(IngredientRepository.class);
    private final IdUtils idUtils = mock(IdUtils.class);
    private final TimeUtils timeUtils = mock(TimeUtils.class);

    private final RecipeService recipeService = new RecipeService(recipeRepository, ingredientRepository, idUtils, timeUtils);

    @Captor
    private ArgumentCaptor<Recipe> argumentCaptor;

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

        when(recipeRepository.findAllByPrivacyIsFalseOrOwner_Id(null, Sort.by(RecipeSorting.RELEVANCE.value).descending())).thenReturn(List.of(r1));

        //WHEN

        List<Recipe> result = recipeService.getAllRecipes(null, RecipeSorting.RELEVANCE);

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

        when(recipeRepository.findAllByPrivacyIsFalseOrOwner_Id("1", Sort.by(RecipeSorting.RELEVANCE.value).descending())).thenReturn(List.of(r1, r2));

        //WHEN

        List<Recipe> result = recipeService.getAllRecipes(chefUser1.getId(), RecipeSorting.RELEVANCE);

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
        Instant now = Instant.now();

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

        IngredientCreationDto ingredientCreationDto = IngredientCreationDto.builder()
                .text("test-ingredient")
                .amount(20)
                .build();

        RecipeCreationDto creationDto = RecipeCreationDto.builder()
                .privacy(false)
                .name("test-name")
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .ingredients(List.of(ingredientCreationDto))
                .build();

        Recipe expected = Recipe.builder()
                .id("1")
                .owner(chefUser1)
                .name("test-name")
                .privacy(false)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(now)
                .ingredients(List.of())
                .build();

        when(recipeRepository.save(any())).thenReturn(expected);
        when(timeUtils.now()).thenReturn(now);
        when(idUtils.generateId()).thenReturn("uuid-1");

        //WHEN

        Recipe result = recipeService.addRecipe(creationDto, chefUser1.getId());

        //THEN

        assertThat(result.getId(), Matchers.is(expected.getId()));
        assertThat(result.isPrivacy(), Matchers.is(expected.isPrivacy()));
        assertThat(result.getOwner().getId(), Matchers.is(expected.getOwner().getId()));
        assertThat(result.getCreatedAt(), Matchers.is(now));
        assertThat(result.getName(), Matchers.is(expected.getName()));
        assertThat(result.getDifficulty(), Matchers.is(expected.getDifficulty()));
        assertThat(result.getDuration(), Matchers.is(expected.getDuration()));
        assertThat(result.getInstruction(), Matchers.is(expected.getInstruction()));
        assertThat(result.getPortions(), Matchers.is(expected.getPortions()));
        assertThat(result.getThumbnail(), Matchers.is(expected.getThumbnail()));

        if (result.getIngredients() != null) {
            for (int i = 0; i < result.getIngredients().size(); i++) {
                assertThat(result.getIngredients().get(i).getText(), Matchers.is(expected.getIngredients().get(i).getText()));
            }
        }

        if (result.getImages() != null) {
            for (int i = 0; i < result.getImages().size(); i++) {
                assertThat(result.getImages().get(i).getId(), Matchers.is(expected.getImages().get(i).getId()));
            }
        }

        if (result.getTaggings() != null) {
            for (int i = 0; i < result.getTaggings().size(); i++) {
                assertThat(result.getTaggings().get(i).getTag().getId(), Matchers.is(expected.getTaggings().get(i).getTag().getId()));
            }
        }
    }

    @Test
    void changeRecipe_valid() {
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

        Ingredient ingredient1 = Ingredient.builder()
                .id("1")
                .text("test-ingredient")
                .amount(20)
                .build();

        Recipe originalRecipe = Recipe.builder()
                .privacy(false)
                .owner(chefUser1)
                .name("test-name-old")
                .portions(6)
                .instruction("test-instructions-old")
                .duration(20)
                .difficulty(Difficulty.MEDIUM)
                .ingredients(List.of(ingredient1))
                .build();

        Ingredient ingredient2 = Ingredient.builder()
                .text("test-ingredient")
                .amount(20)
                .build();

        RecipeEditDto recipeEditDto = RecipeEditDto.builder()
                .privacy(true)
                .name("test-name")
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .ingredients(List.of(ingredient1, ingredient2))
                .build();

        Ingredient ingredientResult1 = Ingredient.builder()
                .id("14")
                .text("test-ingredient")
                .amount(20)
                .build();

        Ingredient ingredientResult2 = Ingredient.builder()
                .id("15")
                .text("test-ingredient")
                .amount(20)
                .build();

        Recipe r1Result = Recipe.builder()
                .id("1")
                .owner(chefUser1)
                .name("test-name")
                .privacy(true)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of(ingredientResult1, ingredientResult2))
                .build();

        when(recipeRepository.findById("1")).thenReturn(Optional.ofNullable(originalRecipe));
        when(recipeRepository.save(any())).thenReturn(r1Result);
        when(idUtils.generateId()).thenReturn("14");
        when(idUtils.generateId()).thenReturn("15");
        doNothing().when(ingredientRepository).deleteById("1");

        //WHEN

        Recipe result = recipeService.changeRecipe("1", recipeEditDto, chefUser1.getId());

        //THEN

        assertThat(result.getId(), Matchers.is(r1Result.getId()));
        assertThat(result.isPrivacy(), Matchers.is(r1Result.isPrivacy()));
        assertThat(result.getOwner().getId(), Matchers.is(r1Result.getOwner().getId()));
        assertThat(result.getName(), Matchers.is(r1Result.getName()));
        assertThat(result.getDifficulty(), Matchers.is(r1Result.getDifficulty()));
        assertThat(result.getDuration(), Matchers.is(r1Result.getDuration()));
        assertThat(result.getInstruction(), Matchers.is(r1Result.getInstruction()));
        assertThat(result.getPortions(), Matchers.is(r1Result.getPortions()));
        assertThat(result.getThumbnail(), Matchers.is(r1Result.getThumbnail()));

        if (result.getIngredients() != null) {
            for (int i = 0; i < result.getIngredients().size(); i++) {
                assertThat(result.getIngredients().get(i).getText(), Matchers.is(r1Result.getIngredients().get(i).getText()));
            }
        }

        if (result.getImages() != null) {
            for (int i = 0; i < result.getImages().size(); i++) {
                assertThat(result.getImages().get(i).getId(), Matchers.is(r1Result.getImages().get(i).getId()));
            }
        }

        if (result.getTaggings() != null) {
            for (int i = 0; i < result.getTaggings().size(); i++) {
                assertThat(result.getTaggings().get(i).getTag().getId(), Matchers.is(r1Result.getTaggings().get(i).getTag().getId()));
            }
        }
    }

    @Test
    void changeRecipe_newRecipe() {
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

        Ingredient ingredient1 = Ingredient.builder()
                .id("1")
                .text("test-ingredient")
                .amount(20)
                .build();

        Ingredient ingredient2 = Ingredient.builder()
                .text("test-ingredient")
                .amount(20)
                .build();

        RecipeEditDto recipeEditDto = RecipeEditDto.builder()
                .privacy(true)
                .name("test-name")
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .ingredients(List.of(ingredient1, ingredient2))
                .build();

        Ingredient ingredientResult1 = Ingredient.builder()
                .id("14")
                .text("test-ingredient")
                .amount(20)
                .build();

        Ingredient ingredientResult2 = Ingredient.builder()
                .id("15")
                .text("test-ingredient")
                .amount(20)
                .build();

        Recipe r1Result = Recipe.builder()
                .id("1")
                .owner(chefUser1)
                .name("test-name")
                .privacy(true)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of(ingredientResult1, ingredientResult2))
                .build();

        when(recipeRepository.findById("1")).thenReturn(Optional.empty());
        when(recipeRepository.save(any())).thenReturn(r1Result);
        when(idUtils.generateId()).thenReturn("14");
        when(idUtils.generateId()).thenReturn("15");

        //WHEN

        Recipe result = recipeService.changeRecipe("1", recipeEditDto, chefUser1.getId());

        //THEN

        assertThat(result.getId(), Matchers.is(r1Result.getId()));
        assertThat(result.isPrivacy(), Matchers.is(r1Result.isPrivacy()));
        assertThat(result.getOwner().getId(), Matchers.is(r1Result.getOwner().getId()));
        assertThat(result.getName(), Matchers.is(r1Result.getName()));
        assertThat(result.getDifficulty(), Matchers.is(r1Result.getDifficulty()));
        assertThat(result.getDuration(), Matchers.is(r1Result.getDuration()));
        assertThat(result.getInstruction(), Matchers.is(r1Result.getInstruction()));
        assertThat(result.getPortions(), Matchers.is(r1Result.getPortions()));
        assertThat(result.getThumbnail(), Matchers.is(r1Result.getThumbnail()));

        if (result.getIngredients() != null) {
            for (int i = 0; i < result.getIngredients().size(); i++) {
                assertThat(result.getIngredients().get(i).getText(), Matchers.is(r1Result.getIngredients().get(i).getText()));
            }
        }

        if (result.getImages() != null) {
            for (int i = 0; i < result.getImages().size(); i++) {
                assertThat(result.getImages().get(i).getId(), Matchers.is(r1Result.getImages().get(i).getId()));
            }
        }

        if (result.getTaggings() != null) {
            for (int i = 0; i < result.getTaggings().size(); i++) {
                assertThat(result.getTaggings().get(i).getTag().getId(), Matchers.is(r1Result.getTaggings().get(i).getTag().getId()));
            }
        }

        assertThat(result, Matchers.is(r1Result));
    }

    @Test
    void recalculateRecipe() {
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
                .images(List.of(Image.builder()
                        .owner(chefUser1)
                        .id("1")
                        .build()))
                .ingredients(List.of())
                .ratings(List.of(Rating.builder()
                        .recipe(Recipe.ofId("1"))
                        .user(chefUser1)
                        .value(5)
                        .build()))
                .averageRating(0.0)
                .relevance(BigInteger.ZERO)
                .build();

        Recipe expected = Recipe.builder()
                .id("1")
                .owner(chefUser1)
                .privacy(true)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .images(List.of(Image.builder()
                        .owner(chefUser1)
                        .id("1")
                        .build()))
                .ingredients(List.of())
                .ratings(List.of(Rating.builder()
                        .recipe(Recipe.ofId("1"))
                        .user(chefUser1)
                        .value(5)
                        .build()))
                .averageRating(5.0)
                .relevance(BigInteger.TWO)
                .build();

        when(recipeRepository.findById("1")).thenReturn(Optional.ofNullable(r1));
        when(recipeRepository.save(argumentCaptor.capture())).thenReturn(expected);

        //WHEN

        recipeService.recalculateRecipe("1");

        //THEN
        verify(recipeRepository).findById("1");
        verify(recipeRepository).save(expected);
        assertThat(argumentCaptor.getValue().getRelevance(), Matchers.is(BigInteger.valueOf(52)));
        assertThat(argumentCaptor.getValue().getAverageRating(), Matchers.is(5.0));

    }
}
