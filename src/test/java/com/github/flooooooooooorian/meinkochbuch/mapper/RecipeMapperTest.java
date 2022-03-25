package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.BaseIngredient;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.models.tag.Tag;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

class RecipeMapperTest {

    @Test
    void recipeToRecipePreviewDto() {
        //GIVEN
        Recipe recipe = MapperUtils.exampleRecipe;

        //WHEN
        RecipePreviewDto result = RecipeMapper.recipeToRecipePreviewDto(recipe);

        //THEN
        RecipePreviewDto expected = RecipePreviewDto.builder()
                .id("1")
                .name("test-name")
                .owner(ChefUserPreviewDto.builder()
                        .id("1")
                        .name("test-name")
                        .build())
                .ratingAverage(BigDecimal.valueOf(4L))
                .ratingCount(1)
                .thumbnail(ImageDto.builder()
                        .id("1")
                        .build())
                .build();
        assertThat(result, Matchers.is(expected));
    }

    @Test
    void recipeWithNullToRecipePreviewDto() {
        //GIVEN
        Recipe recipe = Recipe.builder()
                .id("1")
                .name("test-name")
                .createdAt(Instant.now())
                .owner(ChefUser.builder()
                        .id("1")
                        .name("test-name")
                        .username("test-username")
                        .password("test-password")
                        .build())
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.HARD)
                .portions(2)
                .privacy(false)
                .relevance(BigInteger.ONE)
                .build();

        //WHEN
        RecipePreviewDto result = RecipeMapper.recipeToRecipePreviewDto(recipe);

        //THEN
        RecipePreviewDto expected = RecipePreviewDto.builder()
                .id("1")
                .name("test-name")
                .owner(ChefUserPreviewDto.builder()
                        .id("1")
                        .name("test-name")
                        .build())
                .ratingAverage(BigDecimal.ZERO)
                .ratingCount(0)
                .build();
        assertThat(result, Matchers.is(expected));
    }

    @Test
    void recipeToRecipeDto() {
        //GIVEN
        Recipe recipe = MapperUtils.exampleRecipe;

        //WHEN
        RecipeDto result = RecipeMapper.recipeToRecipeDto(recipe);

        //THEN
        RecipeDto expected = RecipeDto.builder()
                .id("1")
                .name("test-name")
                .owner(ChefUserPreviewDto.builder()
                        .id("1")
                        .name("test-name")
                        .build())
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.HARD)
                .portions(2)
                .thumbnail(ImageDto.builder()
                        .id("1")
                        .build())
                .images(List.of(ImageDto.builder()
                        .id("2")
                        .build()))
                .ingredients(List.of(Ingredient.builder()
                        .id("1")
                        .amount(BigDecimal.valueOf(20L))
                        .text("test-ingredient-test")
                        .baseIngredient(BaseIngredient.builder()
                                .id("1")
                                .name("test-baseIngredient")
                                .children(Set.of(BaseIngredient.builder()
                                        .build()))
                                .singular("test-baseImgredient-Singular")
                                .synonyms(Set.of("test-baseIngredient-synonym"))
                                .build())
                        .build()))
                .privacy(false)
                .tags(List.of(Tag.builder()
                        .id("1")
                        .name("test-tag")
                        .build()))
                .ratingCount(1)
                .ratingAverage(BigDecimal.valueOf(4L))
                .build();


        //assertThat(result, Matchers.is(expected));
        assertThat(result.getId(), Matchers.is(expected.getId()));
        assertThat(result.getDifficulty(), Matchers.is(expected.getDifficulty()));
        assertThat(result.getDuration(), Matchers.is(expected.getDuration()));
        assertThat(result.getPortions(), Matchers.is(expected.getPortions()));
        assertThat(result.getInstruction(), Matchers.is(expected.getInstruction()));
        assertThat(result.getName(), Matchers.is(expected.getName()));
        assertThat(result.getOwner().getId(), Matchers.is(expected.getOwner().getId()));
        assertThat(result.getRatingAverage(), Matchers.is(expected.getRatingAverage()));
        assertThat(result.getRatingCount(), Matchers.is(expected.getRatingCount()));

        for (int i = 0; i < result.getIngredients().size(); i++) {
            assertThat(result.getIngredients().get(i).getId(), Matchers.is(expected.getIngredients().get(i).getId()));
            assertThat(result.getIngredients().get(i).getAmount(), Matchers.is(expected.getIngredients().get(i).getAmount()));
            assertThat(result.getIngredients().get(i).getText(), Matchers.is(expected.getIngredients().get(i).getText()));
            assertThat(result.getIngredients().get(i).getBaseIngredient().getId(), Matchers.is(expected.getIngredients().get(i).getBaseIngredient().getId()));
        }

    }

    @Test
    void recipeWithNullValuesToRecipeDto() {
        //GIVEN
        Recipe recipe = Recipe.builder()
                .id("1")
                .name("test-name")
                .createdAt(Instant.now())
                .owner(ChefUser.builder()
                        .id("1")
                        .name("test-name")
                        .username("test-username")
                        .password("test-password")
                        .build())
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.HARD)
                .portions(2)
                .privacy(false)
                .relevance(BigInteger.ONE)
                .build();

        //WHEN
        RecipeDto result = RecipeMapper.recipeToRecipeDto(recipe);

        //THEN
        RecipeDto expected = RecipeDto.builder()
                .id("1")
                .name("test-name")
                .owner(ChefUserPreviewDto.builder()
                        .id("1")
                        .name("test-name")
                        .build())
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.HARD)
                .portions(2)
                .images(List.of())
                .ingredients(List.of())
                .privacy(false)
                .tags(List.of())
                .ratingCount(0)
                .ratingAverage(BigDecimal.ZERO)
                .build();


        assertThat(result, Matchers.is(expected));
    }
}