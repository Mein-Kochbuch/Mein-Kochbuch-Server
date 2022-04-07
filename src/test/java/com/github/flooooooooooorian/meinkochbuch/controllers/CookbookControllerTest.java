package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreview;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
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
class CookbookControllerTest extends IntegrationTest {

    @LocalServerPort
    private int port;

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
                        .id("test-recipe-id")
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

}