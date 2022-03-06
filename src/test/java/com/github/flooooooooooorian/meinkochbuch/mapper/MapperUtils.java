package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.BaseIngredient;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.models.tag.RecipeTagging;
import com.github.flooooooooooorian.meinkochbuch.models.tag.Tag;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class MapperUtils {
    public static Recipe exampleRecipe = Recipe.builder()
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
}
