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
                .id(1L)
                .name("test-name")
                .owner(ChefUserPreviewDto.builder()
                        .id(1L)
                        .name("test-name")
                        .build())
                .ratingAverage(BigDecimal.valueOf(4L))
                .ratingCount(1)
                .thumbnail(ImageDto.builder()
                        .id(1L)
                        .url("test-thumbnail-url")
                        .build())
                .build();
        assertThat(result, Matchers.is(expected));
    }

    @Test
    void recipeWithNullToRecipePreviewDto() {
        //GIVEN
        Recipe recipe = Recipe.builder()
                .id(1L)
                .name("test-name")
                .createdAt(Instant.now())
                .owner(ChefUser.builder()
                        .id(1L)
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
                .id(1L)
                .name("test-name")
                .owner(ChefUserPreviewDto.builder()
                        .id(1L)
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
                .id(1L)
                .name("test-name")
                .owner(ChefUserPreviewDto.builder()
                        .id(1L)
                        .name("test-name")
                        .build())
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.HARD)
                .portions(2)
                .thumbnail(ImageDto.builder()
                        .id(1L)
                        .url("test-thumbnail-url")
                        .build())
                .images(List.of(ImageDto.builder()
                        .id(2L)
                        .url("test-img-url")
                        .build()))
                .ingredients(List.of(Ingredient.builder()
                        .id(1L)
                        .amount(BigDecimal.valueOf(20L))
                        .text("test-ingredient-test")
                        .baseIngredient(BaseIngredient.builder()
                                .id(1L)
                                .name("test-baseIngredient")
                                .children(Set.of(BaseIngredient.builder()
                                        .build()))
                                .singular("test-baseImgredient-Singular")
                                .synonyms(Set.of("test-baseIngredient-synonym"))
                                .build())
                        .build()))
                .privacy(false)
                .tags(List.of(Tag.builder()
                        .id(1L)
                        .name("test-tag")
                        .build()))
                .ratingCount(1)
                .ratingAverage(BigDecimal.valueOf(4L))
                .build();


        assertThat(result, Matchers.is(expected));
        for (int i = 0; i < result.getImages().size(); i++) {
            assertThat(result.getImages().get(i).getUrl(), Matchers.is(expected.getImages().get(i).getUrl()));
        }
        for (int i = 0; i < result.getIngredients().size(); i++) {
            assertThat(result.getIngredients().get(i), Matchers.is(expected.getIngredients().get(i)));
            assertThat(result.getIngredients().get(i).getBaseIngredient(), Matchers.is(expected.getIngredients().get(i).getBaseIngredient()));
        }

    }

    @Test
    void recipeWithNullValuesToRecipeDto() {
        //GIVEN
        Recipe recipe = Recipe.builder()
                .id(1L)
                .name("test-name")
                .createdAt(Instant.now())
                .owner(ChefUser.builder()
                        .id(1L)
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
                .id(1L)
                .name("test-name")
                .owner(ChefUserPreviewDto.builder()
                        .id(1L)
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