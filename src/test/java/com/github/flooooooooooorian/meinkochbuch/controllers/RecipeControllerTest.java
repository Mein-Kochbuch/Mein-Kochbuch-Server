package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeControllerTest extends IntegrationTest {

    @LocalServerPort
    private int port;

    private WebClient webClient;

    @Test
    void getAllRecipesAnonymous() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-user-id"))
                .instruction("test-recipe-instructions")
                .name("test-recipe-name")
                .build());
        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id-2")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-user-id"))
                .instruction("test-recipe-instructions")
                .name("test-recipe-name")
                .privacy(true)
                .build());

        //WHEN
        List<RecipePreviewDto> result = webClient.get()
                .uri("/recipes")
                .retrieve()
                .toEntityList(RecipePreviewDto.class)
                .block()
                .getBody();

        //THEN
        RecipePreviewDto expected1 = RecipePreviewDto.builder()
                .id("test-recipe-id")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-recipe-name")
                .ratingAverage(BigDecimal.ZERO)
                .ratingCount(0)
                .thumbnail(null)
                .build();

        assertThat(result, containsInAnyOrder(expected1));
    }

    @Test
    void getAllRecipesOwnAndPublic() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-user-id"))
                .instruction("test-recipe-instructions")
                .name("test-recipe-name")
                .build());
        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id-2")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-user-id"))
                .instruction("test-recipe-instructions")
                .name("test-recipe-name")
                .privacy(true)
                .build());
        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id-3")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-admin-id"))
                .instruction("test-recipe-instructions")
                .name("test-recipe-name")
                .build());
        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id-4")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-admin-id"))
                .instruction("test-recipe-instructions")
                .name("test-recipe-name")
                .privacy(true)
                .build());

        //WHEN
        List<RecipePreviewDto> result = webClient.get()
                .uri("/recipes")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntityList(RecipePreviewDto.class)
                .block()
                .getBody();

        //THEN
        RecipePreviewDto expected1 = RecipePreviewDto.builder()
                .id("test-recipe-id")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-recipe-name")
                .ratingAverage(BigDecimal.ZERO)
                .ratingCount(0)
                .thumbnail(null)
                .build();

        RecipePreviewDto expected2 = RecipePreviewDto.builder()
                .id("test-recipe-id-2")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-recipe-name")
                .ratingAverage(BigDecimal.ZERO)
                .ratingCount(0)
                .thumbnail(null)
                .build();

        RecipePreviewDto expected3 = RecipePreviewDto.builder()
                .id("test-recipe-id-3")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-admin-id")
                        .name("some-admin-name")
                        .build())
                .name("test-recipe-name")
                .ratingAverage(BigDecimal.ZERO)
                .ratingCount(0)
                .thumbnail(null)
                .build();

        assertThat(result, containsInAnyOrder(expected1, expected2, expected3));
    }

    @Test
    void getRecipeByIdAnonymousPublic() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-user-id"))
                .instruction("test-recipe-instructions")
                .name("test-recipe-name")
                .build());

        //WHEN
        RecipeDto result = webClient.get()
                .uri("/recipes/test-recipe-id")
                .retrieve()
                .toEntity(RecipeDto.class)
                .block()
                .getBody();

        //THEN
        RecipeDto expected = RecipeDto.builder()
                .id("test-recipe-id")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-recipe-name")
                .ratingAverage(BigDecimal.ZERO)
                .ratingCount(0)
                .thumbnail(null)
                .build();

        assertThat(result, is(expected));
    }

    @Test
    void getRecipeByIdNotExisting() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");


        //WHEN
        ResponseEntity result = webClient.get()
                .uri("/recipes/test-recipe-id")
                .retrieve()
                .toEntity(RecipeDto.class)
                .block();

        //THEN

        assertThat(result.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void addRecipe() {
    }
}
