package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FavoriteControllerTest extends IntegrationTest {

    @LocalServerPort
    private int port;

    private WebClient webClient;

    @Test
    void favorizeRecipe_firstFavorite() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN

        ResponseEntity<Boolean> result = webClient.post()
                .uri("/favorites/test-recipe-id-1")
                .header("Authorization", "Bearer " + getTokenByUserId("some-admin-id"))
                .retrieve()
                .toEntity(Boolean.class)
                .block();

        //THEN
        assertThat(result, Matchers.notNullValue());
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(result.getBody(), Matchers.is(true));
    }

    @Test
    void favorizeRecipe_newFavorite() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN

        ResponseEntity<Boolean> result = webClient.post()
                .uri("/favorites/test-recipe-id-2")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntity(Boolean.class)
                .block();

        //THEN
        assertThat(result, Matchers.notNullValue());
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(result.getBody(), Matchers.is(true));
    }

    @Test
    void favorizeRecipe_alreadyFavorite() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN

        ResponseEntity<Boolean> result = webClient.post()
                .uri("/favorites/test-recipe-id-1")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntity(Boolean.class)
                .block();

        //THEN
        assertThat(result, Matchers.notNullValue());
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(result.getBody(), Matchers.is(false));
    }
}