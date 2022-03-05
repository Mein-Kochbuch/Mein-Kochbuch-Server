package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.BaseIngredient;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.models.tag.RecipeTagging;
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
                .thumbnail(Image.builder()
                        .id(1L)
                        .owner(ChefUser.ofId(1L))
                        .url("test-thumbnail-url")
                        .build())
                .images(List.of(Image.builder()
                        .id(2L)
                        .owner(ChefUser.ofId(1L))
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
                .relevance(BigInteger.ONE)
                .taggings(List.of(RecipeTagging.builder()
                        .tag(Tag.builder()
                                .id(1L)
                                .name("test-tag")
                                .build())
                        .build()))
                .ratings(List.of(Rating.builder()
                        .id(1L)
                        .user(ChefUser.ofId(1L))
                        .value(BigDecimal.valueOf(4L))
                        .build()))
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
    void recipeToRecipeDto() {
    }
}