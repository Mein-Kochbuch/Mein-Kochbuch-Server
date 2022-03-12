package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.CookbookContent;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.BaseIngredient;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.models.tag.RecipeTagging;
import com.github.flooooooooooorian.meinkochbuch.models.tag.Tag;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.models.oauth.OAuthDataApple;
import com.github.flooooooooooorian.meinkochbuch.security.models.oauth.OAuthDataGoogle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class MapperUtils {
    public static Recipe exampleRecipe = Recipe.builder()
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
            .thumbnail(Image.builder()
                    .id("1")
                    .owner(ChefUser.ofId("1"))
                    .build())
            .images(List.of(Image.builder()
                    .id("2")
                    .owner(ChefUser.ofId("1"))
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
            .relevance(BigInteger.ONE)
            .taggings(List.of(RecipeTagging.builder()
                    .tag(Tag.builder()
                            .id("1")
                            .name("test-tag")
                            .build())
                    .build()))
            .ratings(List.of(Rating.builder()
                    .id("1")
                    .user(ChefUser.ofId("1"))
                    .value(BigDecimal.valueOf(4L))
                    .build()))
            .build();


    static ChefUser exampleUser = ChefUser.builder()
            .id("1")
            .password("test-password")
            .username("test@email.com")
            .name("test-name")
            .accountNonExpired(true)
            .accountNonLocked(true)
            .authorities(Set.of(ChefAuthorities.USER))
            .credentialsNonExpired(true)
            .enabled(true)
            .cookbooks(List.of(Cookbook.builder()
                    .id("1")
                    .name("cookbook-name")
                    .owner(ChefUser.builder()
                            .id("1")
                            .name("test-name")
                            .build())
                    .contents(List.of(CookbookContent.builder()
                            .recipe(Recipe.builder()
                                    .id("1")
                                    .owner(ChefUser.builder()
                                            .id("1")
                                            .name("test-name")
                                            .build())
                                    .name("test-recipe")
                                    .duration(40)
                                    .build())
                            .build()))
                    .privacy(false)
                    .thumbnail(Image.builder()
                            .id("1")
                            .build())
                    .build()))
            .recipes(List.of(Recipe.builder()
                    .id("1")
                    .owner(ChefUser.builder()
                            .id("1")
                            .name("test-name")
                            .build())
                    .name("test-recipe")
                    .duration(40)
                    .build()))
            .images(List.of(Image.builder()
                    .id("1")
                    .build()))
            .favoriteRecipes(List.of(
                    Recipe.builder()
                            .id("1")
                            .owner(ChefUser.builder()
                                    .id("1")
                                    .name("test-name")
                                    .build())
                            .name("test-recipe")
                            .duration(40)
                            .build()
            ))
            .oAuthDataApple(OAuthDataApple.builder().build())
            .oAuthDataGoogle(OAuthDataGoogle.builder().build())
            .build();
}
