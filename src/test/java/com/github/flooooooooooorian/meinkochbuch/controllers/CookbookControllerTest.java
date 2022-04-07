package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreview;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

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
}