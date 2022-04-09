package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreview;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CookbookControllerTest extends IntegrationTest {

    @LocalServerPort
    private int port;

    @MockBean
    private IdUtils idUtils;

    private WebClient webClient;

    @Test
    void getAllRecipesAnonymous() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN
        ResponseEntity<List<CookbookPreview>> result = webClient.get()
                .uri("/cookbooks")
                .retrieve()
                .toEntityList(CookbookPreview.class)
                .block();

        //THEN

        CookbookPreview expected1 = CookbookPreview.builder()
                .id("test-cookbook-id-1")
                .name("test-cookbook-name-1")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-user-name")
                        .id("some-user-id")
                        .build())
                .ratingAverage(3)
                .build();

        CookbookPreview expected2 = CookbookPreview.builder()
                .id("test-cookbook-id-3")
                .name("test-cookbook-name-3")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-admin-name")
                        .id("some-admin-id")
                        .build())
                .ratingAverage(3)
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
        ResponseEntity<List<CookbookPreview>> result = webClient.get()
                .uri("/cookbooks")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntityList(CookbookPreview.class)
                .block();

        //THEN

        CookbookPreview expected1 = CookbookPreview.builder()
                .id("test-cookbook-id-1")
                .name("test-cookbook-name-1")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-user-name")
                        .id("some-user-id")
                        .build())
                .ratingAverage(3)
                .build();

        CookbookPreview expected2 = CookbookPreview.builder()
                .id("test-cookbook-id-3")
                .name("test-cookbook-name-3")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-admin-name")
                        .id("some-admin-id")
                        .build())
                .ratingAverage(3)
                .build();

        CookbookPreview expected3 = CookbookPreview.builder()
                .id("test-cookbook-id-2")
                .name("test-cookbook-name-2")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-user-name")
                        .id("some-user-id")
                        .build())
                .ratingAverage(3)
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), containsInAnyOrder(expected1, expected2, expected3));
    }

    @Test
    void getCookbookByIdAnonymousPublic() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN
        ResponseEntity<CookbookDto> result = webClient.get()
                .uri("/cookbooks/test-cookbook-id-1")
                .retrieve()
                .toEntity(CookbookDto.class)
                .block();

        //THEN
        CookbookDto expected = CookbookDto.builder()
                .id("test-cookbook-id-1")
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .name("test-cookbook-name-1")
                .recipes(List.of(RecipePreviewDto.builder()
                        .id("test-recipe-id-1")
                        .owner(ChefUserPreviewDto.builder()
                                .id("some-user-id")
                                .name("some-user-name")
                                .build())
                        .name("test-recipe-name")
                        .ratingAverage(3)
                        .ratingCount(1)
                        .build()))
                .ratingAverage(3)
                .thumbnail(null)
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), is(expected));
    }

    @Test
    void getCookbookByIdAnonymousPrivate() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN
        ResponseEntity<CookbookDto> result = webClient.get()
                .uri("/cookbooks/test-cookbook-id-2")
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.empty())
                .toEntity(CookbookDto.class)
                .block();

        //THEN
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    void getCookbookByIdNotExisting() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");


        //WHEN
        ResponseEntity<CookbookDto> result = webClient.get()
                .uri("/cookbooks/test-cookbook-id-not-found")
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.empty())
                .toEntity(CookbookDto.class)
                .block();

        //THEN

        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void createCookbookOnlyPublic() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        when(idUtils.generateId()).thenReturn("test-cookbook-id");

        CookbookCreationDto cookbookCreationDto = CookbookCreationDto.builder()
                .privacy(false)
                .name("test-cookbook-name")
                .recipeIds(List.of("test-recipe-id-1", "test-recipe-id-3"))
                .build();

        //WHEN

        CookbookDto actual = webClient.post()
                .uri("/cookbooks")
                .header(HttpHeaders.AUTHORIZATION, getTokenByUserId("some-user-id"))
                .bodyValue(cookbookCreationDto)
                .retrieve()
                .toEntity(CookbookDto.class)
                .block()
                .getBody();

        //THEN
        CookbookDto expected = CookbookDto.builder()
                .id("test-cookbook-id")
                .name("test-cookbook-name")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-user-name")
                        .id("some-user-id")
                        .build())
                .recipes(List.of(RecipePreviewDto.builder()
                                .id("test-recipe-id-1")
                                .name("test-recipe-name")
                                .ratingCount(1)
                                .ratingAverage(3)
                                .owner(ChefUserPreviewDto.builder()
                                        .name("some-user-name")
                                        .id("some-user-id")
                                        .build())
                                .build(),
                        RecipePreviewDto.builder()
                                .id("test-recipe-id-3")
                                .name("test-recipe-name")
                                .ratingCount(0)
                                .ratingAverage(0)
                                .owner(ChefUserPreviewDto.builder()
                                        .name("some-admin-name")
                                        .id("some-admin-id")
                                        .build())
                                .build()))
                .privacy(false)
                .ratingAverage(3)
                .build();

        assertThat(actual, is(expected));
    }

    @Test
    void createCookbookWithPrivacy() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        when(idUtils.generateId()).thenReturn("test-cookbook-id");

        CookbookCreationDto cookbookCreationDto = CookbookCreationDto.builder()
                .privacy(false)
                .name("test-cookbook-name")
                .recipeIds(List.of("test-recipe-id-1", "test-recipe-id-2"))
                .build();

        //WHEN

        CookbookDto actual = webClient.post()
                .uri("/cookbooks")
                .header(HttpHeaders.AUTHORIZATION, getTokenByUserId("some-user-id"))
                .bodyValue(cookbookCreationDto)
                .retrieve()
                .toEntity(CookbookDto.class)
                .block()
                .getBody();

        //THEN
        CookbookDto expected = CookbookDto.builder()
                .id("test-cookbook-id")
                .name("test-cookbook-name")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-user-name")
                        .id("some-user-id")
                        .build())
                .recipes(List.of(RecipePreviewDto.builder()
                                .id("test-recipe-id-1")
                                .name("test-recipe-name")
                                .ratingCount(1)
                                .ratingAverage(3)
                                .owner(ChefUserPreviewDto.builder()
                                        .name("some-user-name")
                                        .id("some-user-id")
                                        .build())
                                .build(),
                        RecipePreviewDto.builder()
                                .id("test-recipe-id-2")
                                .name("test-recipe-name")
                                .ratingCount(0)
                                .ratingAverage(0)
                                .owner(ChefUserPreviewDto.builder()
                                        .name("some-user-name")
                                        .id("some-user-id")
                                        .build())
                                .build()))
                .privacy(true)
                .ratingAverage(3)
                .build();

        assertThat(actual, is(expected));
    }
}