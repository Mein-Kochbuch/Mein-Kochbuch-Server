package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.ingredient.IngredientCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeEditDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

        //WHEN
        ResponseEntity<List<RecipePreviewDto>> result = webClient.get()
                .uri("/recipes")
                .retrieve()
                .toEntityList(RecipePreviewDto.class)
                .block();

        //THEN
        RecipePreviewDto expected1 = RecipePreviewDto.builder()
                .id("test-recipe-id-1")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-recipe-name")
                .ratingAverage(3)
                .ratingCount(1)
                .thumbnail(null)
                .build();

        RecipePreviewDto expected2 = RecipePreviewDto.builder()
                .id("test-recipe-id-3")
                .name("test-recipe-name")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-admin-id")
                        .name("some-admin-name")
                        .build())
                .ratingAverage(0)
                .ratingCount(0)
                .thumbnail(null)
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), containsInAnyOrder(expected1, expected2));
    }

    @Test
    void getAllRecipesOwnAndPublic() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN
        ResponseEntity<List<RecipePreviewDto>> result = webClient.get()
                .uri("/recipes")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntityList(RecipePreviewDto.class)
                .block();

        //THEN
        RecipePreviewDto expected1 = RecipePreviewDto.builder()
                .id("test-recipe-id-1")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-recipe-name")
                .ratingAverage(3)
                .ratingCount(1)
                .thumbnail(null)
                .build();

        RecipePreviewDto expected2 = RecipePreviewDto.builder()
                .id("test-recipe-id-2")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-recipe-name")
                .ratingAverage(0)
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
                .ratingAverage(0)
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

        //WHEN
        ResponseEntity<RecipeDto> result = webClient.get()
                .uri("/recipes/test-recipe-id-1")
                .retrieve()
                .toEntity(RecipeDto.class)
                .block();

        //THEN
        RecipeDto expected = RecipeDto.builder()
                .id("test-recipe-id-1")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-recipe-name")
                .instruction("test-recipe-instructions")
                .ratingAverage(3)
                .ratingCount(1)
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

        //WHEN
        ResponseEntity<RecipeDto> result = webClient.get()
                .uri("/recipes/test-recipe-id-2")
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
                .uri("/recipes/test-recipe-id-not-found")
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

        RecipeCreationDto editRecipeDto = RecipeCreationDto.builder()
                .name("test-recipe-name")
                .difficulty(Difficulty.MEDIUM)
                .duration(40)
                .ingredients(List.of(IngredientCreationDto.builder()
                        .text("test-ingredient-text")
                        .amount(10)
                        .build()))
                .portions(4)
                .instruction("test-instructions")
                .privacy(false)
                .build();


        //WHEN

        ResponseEntity<RecipeDto> result = webClient.post()
                .uri("/recipes")
                .bodyValue(editRecipeDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntity(RecipeDto.class)
                .block();

        //THEN

        assertThat(result, notNullValue());
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getId(), notNullValue());
        assertThat(result.getBody().getName(), is(editRecipeDto.getName()));
        assertThat(result.getBody().getInstruction(), is(editRecipeDto.getInstruction()));
        assertThat(result.getBody().getDifficulty(), is(editRecipeDto.getDifficulty()));
        assertThat(result.getBody().getDuration(), is(editRecipeDto.getDuration()));
        assertThat(result.getBody().getImages(), empty());
        assertThat(result.getBody().getOwner().getId(), is("some-user-id"));
        assertThat(result.getBody().getIngredients().stream().map(Ingredient::getText).toList(), is(editRecipeDto.getIngredients().stream().map(IngredientCreationDto::getText).toList()));
        assertThat(result.getBody().getPortions(), is(editRecipeDto.getPortions()));
        assertThat(result.getBody().getRatingAverage(), is(0.0));
        assertThat(result.getBody().getRatingCount(), is(0));
        assertThat(result.getBody().getTags(), empty());
        assertThat(result.getBody().getThumbnail(), nullValue());
        assertThat(result.getBody().isPrivacy(), is(editRecipeDto.isPrivacy()));
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
                        .amount(10)
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

    @Test
    void changeRecipe_valid() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        RecipeEditDto editRecipeDto = RecipeEditDto.builder()
                .name("test-recipe-name")
                .difficulty(Difficulty.MEDIUM)
                .duration(40)
                .ingredients(List.of(Ingredient.builder()
                                .id("1")
                                .text("test-ingredient-text")
                                .amount(10)
                                .build(),
                        Ingredient.builder()
                                .text("test-ingredient-text")
                                .amount(10)
                                .build()))
                .portions(4)
                .instruction("test-instructions")
                .privacy(false)
                .build();


        //WHEN

        ResponseEntity<RecipeDto> result = webClient.put()
                .uri("/recipes/test-recipe-id-1")
                .bodyValue(editRecipeDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntity(RecipeDto.class)
                .block();

        //THEN

        assertThat(result, notNullValue());
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getId(), notNullValue());
        assertThat(result.getBody().getName(), is(editRecipeDto.getName()));
        assertThat(result.getBody().getInstruction(), is(editRecipeDto.getInstruction()));
        assertThat(result.getBody().getDifficulty(), is(editRecipeDto.getDifficulty()));
        assertThat(result.getBody().getDuration(), is(editRecipeDto.getDuration()));
        assertThat(result.getBody().getImages(), empty());
        assertThat(result.getBody().getOwner().getId(), is("some-user-id"));
        assertThat(result.getBody().getIngredients().stream().map(Ingredient::getText).toList(), is(editRecipeDto.getIngredients().stream().map(Ingredient::getText).toList()));
        assertThat(result.getBody().getPortions(), is(editRecipeDto.getPortions()));
        assertThat(result.getBody().getRatingAverage(), is(0.0));
        assertThat(result.getBody().getRatingCount(), is(0));
        assertThat(result.getBody().getTags(), empty());
        assertThat(result.getBody().getThumbnail(), nullValue());
        assertThat(result.getBody().isPrivacy(), is(editRecipeDto.isPrivacy()));
    }

    @Test
    void changeRecipe_newRecipe() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        RecipeEditDto editRecipeDto = RecipeEditDto.builder()
                .name("test-recipe-name")
                .difficulty(Difficulty.MEDIUM)
                .duration(40)
                .ingredients(List.of(Ingredient.builder()
                                .id("1")
                                .text("test-ingredient-text")
                                .amount(10)
                                .build(),
                        Ingredient.builder()
                                .text("test-ingredient-text")
                                .amount(10)
                                .build()))
                .portions(4)
                .instruction("test-instructions")
                .privacy(false)
                .build();


        //WHEN

        ResponseEntity<RecipeDto> result = webClient.put()
                .uri("/recipes/test-recipe-id-15")
                .bodyValue(editRecipeDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntity(RecipeDto.class)
                .block();

        //THEN

        assertThat(result, notNullValue());
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getId(), notNullValue());
        assertThat(result.getBody().getName(), is(editRecipeDto.getName()));
        assertThat(result.getBody().getInstruction(), is(editRecipeDto.getInstruction()));
        assertThat(result.getBody().getDifficulty(), is(editRecipeDto.getDifficulty()));
        assertThat(result.getBody().getDuration(), is(editRecipeDto.getDuration()));
        assertThat(result.getBody().getImages(), empty());
        assertThat(result.getBody().getOwner().getId(), is("some-user-id"));
        assertThat(result.getBody().getIngredients().stream().map(Ingredient::getText).toList(), is(editRecipeDto.getIngredients().stream().map(Ingredient::getText).toList()));
        assertThat(result.getBody().getPortions(), is(editRecipeDto.getPortions()));
        assertThat(result.getBody().getRatingAverage(), is(0.0));
        assertThat(result.getBody().getRatingCount(), is(0));
        assertThat(result.getBody().getTags(), empty());
        assertThat(result.getBody().getThumbnail(), nullValue());
        assertThat(result.getBody().isPrivacy(), is(editRecipeDto.isPrivacy()));
    }

    @Test
    void changeRecipe_notOwn() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        RecipeEditDto editRecipeDto = RecipeEditDto.builder()
                .name("test-recipe-name")
                .difficulty(Difficulty.MEDIUM)
                .duration(40)
                .ingredients(List.of(Ingredient.builder()
                                .id("1")
                                .text("test-ingredient-text")
                                .amount(10)
                                .build(),
                        Ingredient.builder()
                                .text("test-ingredient-text")
                                .amount(10)
                                .build()))
                .portions(4)
                .instruction("test-instructions")
                .privacy(false)
                .build();


        //WHEN

        ResponseEntity<RecipeDto> result = webClient.put()
                .uri("/recipes/test-recipe-id-2")
                .bodyValue(editRecipeDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-admin-id"))
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.empty())
                .toEntity(RecipeDto.class)
                .block();

        //THEN
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.FORBIDDEN));

    }
}
