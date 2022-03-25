package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.ingredient.IngredientCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        ResponseEntity<List<RecipePreviewDto>> result = webClient.get()
                .uri("/recipes")
                .retrieve()
                .toEntityList(RecipePreviewDto.class)
                .block();

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

        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), containsInAnyOrder(expected1));
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
        ResponseEntity<List<RecipePreviewDto>> result = webClient.get()
                .uri("/recipes")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntityList(RecipePreviewDto.class)
                .block();

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

        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), containsInAnyOrder(expected1, expected2, expected3));
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
        ResponseEntity<RecipeDto> result = webClient.get()
                .uri("/recipes/test-recipe-id")
                .retrieve()
                .toEntity(RecipeDto.class)
                .block();

        //THEN
        RecipeDto expected = RecipeDto.builder()
                .id("test-recipe-id")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-recipe-name")
                .instruction("test-recipe-instructions")
                .ratingAverage(BigDecimal.ZERO)
                .ratingCount(0)
                .thumbnail(null)
                .images(List.of())
                .ingredients(List.of())
                .tags(List.of())
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), is(expected));
    }

    @Test
    void getRecipeByIdAnonymousPrivate() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-user-id"))
                .instruction("test-recipe-instructions")
                .name("test-recipe-name")
                .privacy(true)
                .build());

        //WHEN
        ResponseEntity<RecipeDto> result = webClient.get()
                .uri("/recipes/test-recipe-id")
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.empty())
                .toEntity(RecipeDto.class)
                .block();

        //THEN
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    void getRecipeByIdNotExisting() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");


        //WHEN
        ResponseEntity<RecipeDto> result = webClient.get()
                .uri("/recipes/test-recipe-id")
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.empty())
                .toEntity(RecipeDto.class)
                .block();

        //THEN

        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void addRecipe() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        RecipeCreationDto recipeCreationDto = RecipeCreationDto.builder()
                .name("test-recipe-name")
                .difficulty(Difficulty.MEDIUM)
                .duration(40)
                .ingredients(List.of(IngredientCreationDto.builder()
                        .text("test-ingredient-text")
                        .amount(BigDecimal.valueOf(10))
                        .build()))
                .portions(4)
                .instruction("test-instructions")
                .privacy(false)
                .build();


        //WHEN

        ResponseEntity<RecipeDto> result = webClient.post()
                .uri("/recipes")
                .bodyValue(recipeCreationDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntity(RecipeDto.class)
                .block();

        //THEN

        assertThat(result, notNullValue());
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getId(), notNullValue());
        assertThat(result.getBody().getName(), is(recipeCreationDto.getName()));
        assertThat(result.getBody().getInstruction(), is(recipeCreationDto.getInstruction()));
        assertThat(result.getBody().getDifficulty(), is(recipeCreationDto.getDifficulty()));
        assertThat(result.getBody().getDuration(), is(recipeCreationDto.getDuration()));
        assertThat(result.getBody().getImages(), empty());
        assertThat(result.getBody().getOwner().getId(), is("some-user-id"));
        assertThat(result.getBody().getIngredients().stream().map(Ingredient::getText).toList(), is(recipeCreationDto.getIngredients().stream().map(IngredientCreationDto::getText).toList()));
        assertThat(result.getBody().getPortions(), is(recipeCreationDto.getPortions()));
        assertThat(result.getBody().getRatingAverage(), is(BigDecimal.ZERO));
        assertThat(result.getBody().getRatingCount(), is(0));
        assertThat(result.getBody().getTags(), empty());
        assertThat(result.getBody().getThumbnail(), nullValue());
        assertThat(result.getBody().isPrivacy(), is(recipeCreationDto.isPrivacy()));
    }

    @Test
    void addRecipeForbidden() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        RecipeCreationDto recipeCreationDto = RecipeCreationDto.builder()
                .name("test-recipe-name")
                .difficulty(Difficulty.MEDIUM)
                .duration(40)
                .ingredients(List.of(IngredientCreationDto.builder()
                        .text("test-ingredient-text")
                        .amount(BigDecimal.valueOf(10))
                        .build()))
                .portions(4)
                .instruction("test-instructions")
                .privacy(false)
                .build();


        //WHEN

        ResponseEntity<RecipeDto> result = webClient.post()
                .uri("/recipes")
                .bodyValue(recipeCreationDto)
                .retrieve()
                .onStatus(HttpStatus::isError, ex -> Mono.empty())
                .toEntity(RecipeDto.class)
                .block();

        //THEN
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}
