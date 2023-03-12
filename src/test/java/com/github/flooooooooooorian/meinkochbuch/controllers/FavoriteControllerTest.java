package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FavoriteControllerTest extends IntegrationTest {
    @Autowired
    private WebTestClient webClient;

    @Test
    void favorizeRecipe_firstFavorite() {
        //GIVEN
        //WHEN

        Boolean result = webClient.post()
                .uri("/api/favorites/test-recipe-id-1")
                .header("Authorization", "Bearer " + getTokenByUserId("some-admin-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Boolean.class)
                .returnResult()
                .getResponseBody();

        //THEN
        assertThat(result, Matchers.is(true));
    }

    @Test
    void favorizeRecipe_newFavorite() {
        //GIVEN
        //WHEN

        Boolean result = webClient.post()
                .uri("/api/favorites/test-recipe-id-2")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Boolean.class)
                .returnResult()
                .getResponseBody();

        //THEN
        assertThat(result, Matchers.is(true));
    }

    @Test
    void favorizeRecipe_alreadyFavorite() {
        //GIVEN
        //WHEN

        Boolean result = webClient.post()
                .uri("/api/favorites/test-recipe-id-1")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Boolean.class)
                .returnResult()
                .getResponseBody();

        //THEN
        assertThat(result, Matchers.is(false));
    }
}