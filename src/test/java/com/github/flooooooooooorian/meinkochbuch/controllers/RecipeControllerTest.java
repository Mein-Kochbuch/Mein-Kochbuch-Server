package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.controllers.responses.RecipeListResponse;
import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.ingredient.IngredientCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeEditDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeControllerTest extends IntegrationTest {

    @LocalServerPort
    private int port;

    @MockBean
    private S3Client s3Client;

    @MockBean
    private IdUtils idUtils;

    @Autowired
    private WebTestClient webClient;

    @Test
    void getAllRecipesAnonymous() {
        //GIVEN

        //WHEN
        RecipeListResponse result = webClient.get()
                .uri("/api/recipes")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeListResponse.class)
                .returnResult()
                .getResponseBody();


        //THEN
        RecipePreviewDto expected1 = RecipePreviewDto.builder()
                .id("test-recipe-id-1")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-recipe-name-A")
                .ratingAverage(3)
                .ratingCount(1)
                .thumbnail(null)
                .build();

        RecipePreviewDto expected2 = RecipePreviewDto.builder()
                .id("test-recipe-id-3")
                .name("test-recipe-name-C")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-admin-id")
                        .name("some-admin-name")
                        .build())
                .ratingAverage(4.5)
                .ratingCount(2)
                .thumbnail(null)
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getResults(), containsInAnyOrder(expected1, expected2));
    }

    @Test
    void getAllRecipesOwnAndPublic() {
        //GIVEN

        //WHEN
        RecipeListResponse result = webClient.get()
                .uri("/api/recipes")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeListResponse.class)
                .returnResult()
                .getResponseBody();


        //THEN
        RecipePreviewDto expected1 = RecipePreviewDto.builder()
                .id("test-recipe-id-1")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-recipe-name-A")
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
                .name("test-recipe-name-B")
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
                .name("test-recipe-name-C")
                .ratingAverage(4.5)
                .ratingCount(2)
                .thumbnail(null)
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getResults(), containsInAnyOrder(expected1, expected2, expected3));
    }

    @Test
    void getAllRecipesSortDefault() {
        //GIVEN


        //WHEN

        RecipeListResponse result = webClient.get()
                .uri("/api/recipes")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeListResponse.class)
                .returnResult()
                .getResponseBody();

        //THEN
        RecipePreviewDto expected1 = RecipePreviewDto.builder()
                .id("test-recipe-id-3")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-admin-id")
                        .name("some-admin-name")
                        .build())
                .name("test-recipe-name-C")
                .ratingAverage(4.5)
                .ratingCount(2)
                .build();

        RecipePreviewDto expected2 = RecipePreviewDto.builder()
                .id("test-recipe-id-1")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-user-name")
                        .id("some-user-id")
                        .build())
                .name("test-recipe-name-A")
                .ratingCount(1)
                .ratingAverage(3)
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getResults(), Matchers.contains(expected1, expected2));
    }

    @Test
    void getAllRecipesSortCustom() {
        //GIVEN


        //WHEN

        RecipeListResponse result = webClient.get()
                .uri("/api/recipes?sort=ALPHABETICALLY_ASC")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeListResponse.class)
                .returnResult()
                .getResponseBody();

        //THEN
        RecipePreviewDto expected1 = RecipePreviewDto.builder()
                .id("test-recipe-id-3")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-admin-id")
                        .name("some-admin-name")
                        .build())
                .name("test-recipe-name-C")
                .ratingAverage(4.5)
                .ratingCount(2)
                .build();

        RecipePreviewDto expected2 = RecipePreviewDto.builder()
                .id("test-recipe-id-1")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-user-name")
                        .id("some-user-id")
                        .build())
                .name("test-recipe-name-A")
                .ratingCount(1)
                .ratingAverage(3)
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getResults(), Matchers.contains(expected2, expected1));
    }

    @Test
    void getAllRecipesDefaultPage() {
        //GIVEN
        //WHEN

        RecipeListResponse result = webClient.get()
                .uri("/api/recipes")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeListResponse.class)
                .returnResult()
                .getResponseBody();

        //THEN
        RecipePreviewDto expected1 = RecipePreviewDto.builder()
                .id("test-recipe-id-3")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-admin-id")
                        .name("some-admin-name")
                        .build())
                .name("test-recipe-name-C")
                .ratingAverage(4.5)
                .ratingCount(2)
                .build();

        RecipePreviewDto expected2 = RecipePreviewDto.builder()
                .id("test-recipe-id-1")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-user-name")
                        .id("some-user-id")
                        .build())
                .name("test-recipe-name-A")
                .ratingCount(1)
                .ratingAverage(3)
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getResults(), containsInAnyOrder(expected1, expected2));
    }

    @Test
    void getAllRecipesSecondPage() {
        //GIVEN
        //WHEN

        RecipeListResponse result = webClient.get()
                .uri("/api/recipes?page=1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeListResponse.class)
                .returnResult()
                .getResponseBody();

        //THEN
        assertThat(result, notNullValue());
        assertThat(result.getResults(), empty());
    }

    @Test
    void getRecipeByIdAnonymousPublic() {
        //GIVEN

        //WHEN
        RecipeDto result = webClient.get()
                .uri("/api/recipes/test-recipe-id-1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeDto.class)
                .returnResult()
                .getResponseBody();

        //THEN
        RecipeDto expected = RecipeDto.builder()
                .id("test-recipe-id-1")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-recipe-name-A")
                .instruction("test-recipe-instructions")
                .ratingAverage(3)
                .ratingCount(1)
                .thumbnail(null)
                .images(List.of())
                .ingredients(List.of())
                .tags(List.of())
                .build();

        assertThat(result, notNullValue());
        assertThat(result, is(expected));
    }

    @Test
    void getRecipeByIdAnonymousPrivate() {
        //GIVEN
        //WHEN
        webClient.get()
                .uri("/api/recipes/test-recipe-id-2")
                .exchange()
                .expectStatus()
                //THEN
                .isForbidden();
    }

    @Test
    void getRecipeByIdNotExisting() {
        //GIVEN
        //WHEN
        webClient.get()
                .uri("/api/recipes/test-recipe-id-not-found")
                .exchange()
                .expectStatus()
                //THEN
                .isNotFound();
    }

    @Test
    void addRecipeWithOutImage() {
        //GIVEN
        when(idUtils.generateId()).thenReturn("test_id");

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
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("editRecipeDto", editRecipeDto);

        RecipeDto result = webClient.post()
                .uri("/api/recipes")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_MIXED_VALUE)
                .bodyValue(body)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeDto.class)
                .returnResult()
                .getResponseBody();

        //THEN

        assertThat(result, notNullValue());
        assertThat(result.getId(), is("test_id"));
        assertThat(result.getName(), is(editRecipeDto.getName()));
        assertThat(result.getInstruction(), is(editRecipeDto.getInstruction()));
        assertThat(result.getDifficulty(), is(editRecipeDto.getDifficulty()));
        assertThat(result.getDuration(), is(editRecipeDto.getDuration()));
        assertThat(result.getImages(), empty());
        assertThat(result.getOwner().getId(), is("some-user-id"));
        assertThat(result.getIngredients().stream().map(Ingredient::getText).toList(), is(editRecipeDto.getIngredients().stream().map(IngredientCreationDto::getText).toList()));
        assertThat(result.getPortions(), is(editRecipeDto.getPortions()));
        assertThat(result.getRatingAverage(), is(0.0));
        assertThat(result.getRatingCount(), is(0));
        assertThat(result.getTags(), empty());
        assertThat(result.getThumbnail(), nullValue());
        assertThat(result.isPrivacy(), is(editRecipeDto.isPrivacy()));
    }

    @Test
    void addRecipeWithImage() {
        //GIVEN
        when(s3Client.utilities()).thenReturn(S3Utilities.builder()
                .region(Region.EU_CENTRAL_1)
                .build());
        when(idUtils.generateId())
                .thenReturn("test_recipe_id")
                .thenReturn("test_image_id");


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

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("editRecipeDto", editRecipeDto);
        body.add("file", new ClassPathResource("test_img.jpg"));

        RecipeDto result = webClient.post()
                .uri("/api/recipes")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_MIXED_VALUE)
                .bodyValue(body)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeDto.class)
                .returnResult()
                .getResponseBody();

        //THEN

        assertThat(result, notNullValue());
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), is(editRecipeDto.getName()));
        assertThat(result.getInstruction(), is(editRecipeDto.getInstruction()));
        assertThat(result.getDifficulty(), is(editRecipeDto.getDifficulty()));
        assertThat(result.getDuration(), is(editRecipeDto.getDuration()));
        assertThat(result.getImages(), containsInAnyOrder(ImageDto.builder()
                .id("test_image_id")
                .url("https://s3.eu-central-1.amazonaws.com/testBucketName/test_recipe_id.jpg")
                .build()));
        assertThat(result.getOwner().getId(), is("some-user-id"));
        assertThat(result.getIngredients().stream().map(Ingredient::getText).toList(), is(editRecipeDto.getIngredients().stream().map(IngredientCreationDto::getText).toList()));
        assertThat(result.getPortions(), is(editRecipeDto.getPortions()));
        assertThat(result.getRatingAverage(), is(0.0));
        assertThat(result.getRatingCount(), is(0));
        assertThat(result.getTags(), empty());
        assertThat(result.getThumbnail(), is(ImageDto.builder()
                .id("test_image_id")
                .url("https://s3.eu-central-1.amazonaws.com/testBucketName/test_recipe_id.jpg")
                .build()));
        assertThat(result.isPrivacy(), is(editRecipeDto.isPrivacy()));
    }

    @Test
    void addRecipeForbidden() {
        //GIVEN
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

        webClient.post()
                .uri("/api/recipes")
                .bodyValue(recipeCreationDto)
                .exchange()
                .expectStatus()
                //THEN
                .isForbidden();
    }

    @Test
    void changeRecipe_valid() {
        //GIVEN
        when(idUtils.generateId())
                .thenReturn("test_ingredient_id_1")
                .thenReturn("test_ingredient_id_2");


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

        RecipeDto result = webClient.put()
                .uri("/api/recipes/test-recipe-id-1")
                .bodyValue(editRecipeDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeDto.class)
                .returnResult()
                .getResponseBody();
        //THEN

        assertThat(result, notNullValue());
        assertThat(result.getId(), is("test-recipe-id-1"));
        assertThat(result.getName(), is(editRecipeDto.getName()));
        assertThat(result.getInstruction(), is(editRecipeDto.getInstruction()));
        assertThat(result.getDifficulty(), is(editRecipeDto.getDifficulty()));
        assertThat(result.getDuration(), is(editRecipeDto.getDuration()));
        assertThat(result.getImages(), empty());
        assertThat(result.getOwner().getId(), is("some-user-id"));
        assertThat(result.getIngredients().stream().map(Ingredient::getText).toList(), is(editRecipeDto.getIngredients().stream().map(Ingredient::getText).toList()));
        assertThat(result.getPortions(), is(editRecipeDto.getPortions()));
        assertThat(result.getRatingAverage(), is(0.0));
        assertThat(result.getRatingCount(), is(0));
        assertThat(result.getTags(), empty());
        assertThat(result.getThumbnail(), nullValue());
        assertThat(result.isPrivacy(), is(editRecipeDto.isPrivacy()));
    }

    @Test
    void changeRecipe_RecipeDoesntExist_ThrowsException() {
        //GIVEN
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

        webClient.put()
                .uri("/api/api/recipes/test-recipe-id-15")
                .bodyValue(editRecipeDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                //THEN
                .isNotFound();
    }

    @Test
    void changeRecipe_notOwn() {
        //GIVEN
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

        webClient.put()
                .uri("/api/recipes/test-recipe-id-2")
                .bodyValue(editRecipeDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-admin-id"))
                .exchange()
                .expectStatus()
                //THEN
                .isForbidden();
    }
}
