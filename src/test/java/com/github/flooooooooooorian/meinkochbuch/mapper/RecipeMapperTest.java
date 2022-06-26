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

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecipeMapperTest {

    private final ImageMapper imageMapperMock = mock(ImageMapper.class);
    private final ChefUserMapper chefUserMapperMock = mock(ChefUserMapper.class);
    private final RecipeMapper recipeMapper = new RecipeMapper(imageMapperMock, chefUserMapperMock);

    @Test
    void recipeToRecipePreviewDto() {
        //GIVEN
        Recipe recipe = MapperUtils.exampleRecipe;

        when(chefUserMapperMock.chefUserToChefUserPreviewDto(any())).thenReturn(ChefUserPreviewDto.builder()
                .id("1")
                .name("test-name")
                .build());

        when(imageMapperMock.imageToThumbnailDto(any())).thenReturn(ImageDto.builder()
                .id("1")
                .build());

        //WHEN
        RecipePreviewDto result = recipeMapper.recipeToRecipePreviewDto(recipe);

        //THEN
        RecipePreviewDto expected = RecipePreviewDto.builder()
                .id("1")
                .name("test-name")
                .owner(ChefUserPreviewDto.builder()
                        .id("1")
                        .name("test-name")
                        .build())
                .ratingAverage(4)
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

        when(chefUserMapperMock.chefUserToChefUserPreviewDto(any())).thenReturn(ChefUserPreviewDto.builder()
                .id("1")
                .name("test-name")
                .build());

        //WHEN
        RecipePreviewDto result = recipeMapper.recipeToRecipePreviewDto(recipe);

        //THEN
        RecipePreviewDto expected = RecipePreviewDto.builder()
                .id("1")
                .name("test-name")
                .owner(ChefUserPreviewDto.builder()
                        .id("1")
                        .name("test-name")
                        .build())
                .ratingAverage(0)
                .ratingCount(0)
                .build();
        assertThat(result, Matchers.is(expected));
    }

    @Test
    void recipeToRecipeDto() {
        //GIVEN
        Recipe recipe = MapperUtils.exampleRecipe;

        when(chefUserMapperMock.chefUserToChefUserPreviewDto(any())).thenReturn(ChefUserPreviewDto.builder()
                .id("1")
                .name("test-name")
                .build());

        when(imageMapperMock.imageToImageDto(any()))
                .thenReturn(ImageDto.builder()
                        .id("2")
                        .build());
        when(imageMapperMock.imageToThumbnailDto(any()))
                .thenReturn(ImageDto.builder()
                        .id("1")
                        .build());

        //WHEN
        RecipeDto result = recipeMapper.recipeToRecipeDto(recipe);

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
                        .amount(20)
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
                .ratingAverage(4)
                .build();

        assertThat(result, Matchers.is(expected));
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

        when(chefUserMapperMock.chefUserToChefUserPreviewDto(any())).thenReturn(ChefUserPreviewDto.builder()
                .id("1")
                .name("test-name")
                .build());

        //WHEN
        RecipeDto result = recipeMapper.recipeToRecipeDto(recipe);

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
                .ratingAverage(0)
                .build();


        assertThat(result, Matchers.is(expected));
    }
}