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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;

import java.net.MalformedURLException;
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

    private WebClient webClient;

    @Test
    void getAllRecipesAnonymous() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN
        ResponseEntity<RecipeListResponse> result = webClient.get()
                .uri("/recipes")
                .retrieve()
                .toEntity(RecipeListResponse.class)
                .block();

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
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getResults(), containsInAnyOrder(expected1, expected2));
    }

    @Test
    void getAllRecipesOwnAndPublic() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN
        ResponseEntity<RecipeListResponse> result = webClient.get()
                .uri("/recipes")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntity(RecipeListResponse.class)
                .block();

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
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getResults(), containsInAnyOrder(expected1, expected2, expected3));
    }

    @Test
    void getAllRecipesSortDefault() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN

        ResponseEntity<RecipeListResponse> result = webClient.get()
                .uri("/recipes")
                .retrieve()
                .toEntity(RecipeListResponse.class)
                .block();
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
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getResults(), Matchers.contains(expected1, expected2));
    }

    @Test
    void getAllRecipesSortCustom() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN

        ResponseEntity<RecipeListResponse> result = webClient.get()
                .uri("/recipes?sort=ALPHABETICALLY_ASC")
                .retrieve()
                .toEntity(RecipeListResponse.class)
                .block();
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
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getResults(), Matchers.contains(expected2, expected1));
    }

    @Test
    void getAllRecipesDefaultPage() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN

        ResponseEntity<RecipeListResponse> result = webClient.get()
                .uri("/recipes")
                .retrieve()
                .toEntity(RecipeListResponse.class)
                .block();
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
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getResults(), containsInAnyOrder(expected1, expected2));
    }

    @Test
    void getAllRecipesSecondPage() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN

        ResponseEntity<RecipeListResponse> result = webClient.get()
                .uri("/recipes?page=1")
                .retrieve()
                .toEntity(RecipeListResponse.class)
                .block();
        //THEN
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getResults(), empty());
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
    void addRecipeWithOutImage() {
        //GIVEN
        when(idUtils.generateId()).thenReturn("test_id");

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


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("editRecipeDto", editRecipeDto);

        ResponseEntity<RecipeDto> result = webClient.post()
                .uri("/recipes")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_MIXED_VALUE)
                .bodyValue(body)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntity(RecipeDto.class)
                .block();

        //THEN

        assertThat(result, notNullValue());
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getId(), is("test_id"));
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
    void addRecipeWithImage() throws MalformedURLException {
        //GIVEN
//        S3Utilities s3Utilities = mock(S3Utilities.class);
        when(s3Client.utilities()).thenReturn(S3Utilities.builder()
                .region(Region.EU_CENTRAL_1)
                .build());
//        when(s3Utilities.getUrl(Mockito.any(GetUrlRequest.class))).thenReturn(new URL("test_image_url"));
        when(idUtils.generateId())
                .thenReturn("test_recipe_id")
                .thenReturn("test_image_id");

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


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("editRecipeDto", editRecipeDto);
        body.add("file", new ClassPathResource("test_img.jpg"));

        ResponseEntity<RecipeDto> result = webClient.post()
                .uri("/recipes")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_MIXED_VALUE)
                .bodyValue(body)
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
        assertThat(result.getBody().getImages(), containsInAnyOrder(ImageDto.builder()
                .id("test_image_id")
                .url("https://s3.eu-central-1.amazonaws.com/testBucketName/test_recipe_id.jpg")
                .build()));
        assertThat(result.getBody().getOwner().getId(), is("some-user-id"));
        assertThat(result.getBody().getIngredients().stream().map(Ingredient::getText).toList(), is(editRecipeDto.getIngredients().stream().map(IngredientCreationDto::getText).toList()));
        assertThat(result.getBody().getPortions(), is(editRecipeDto.getPortions()));
        assertThat(result.getBody().getRatingAverage(), is(0.0));
        assertThat(result.getBody().getRatingCount(), is(0));
        assertThat(result.getBody().getTags(), empty());
        assertThat(result.getBody().getThumbnail(), is(ImageDto.builder()
                .id("test_image_id")
                .url("https://s3.eu-central-1.amazonaws.com/testBucketName/test_recipe_id.jpg")
                .build()));
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
        when(idUtils.generateId())
                .thenReturn("test_ingredient_id_1")
                .thenReturn("test_ingredient_id_2");
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
        assertThat(result.getBody().getId(), is("test-recipe-id-1"));
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
        when(idUtils.generateId())
                .thenReturn("test_ingredient_id_1")
                .thenReturn("test_ingredient_id_2")
                .thenReturn("test_recipe_id");

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
        assertThat(result.getBody().getId(), is("test_recipe_id"));
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
