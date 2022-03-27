package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.dtos.rating.CreateRatingDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.rating.RatingDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RatingControllerTest extends IntegrationTest {

    @LocalServerPort
    private int port;

    private WebClient webClient;

    @Test
    void getOwnRatingFromRecipe_valid() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN

        ResponseEntity<RatingDto> result = webClient.get()
                .uri("/ratings/test-recipe-id")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntity(RatingDto.class)
                .block();

        //THEN
        RatingDto expected = RatingDto.builder()
                .recipeId("test-recipe-id")
                .rating(3)
                .build();

        assertThat(result, Matchers.notNullValue());
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(result.getBody(), Matchers.is(expected));
    }

    @Test
    void getOwnRatingFromRecipe_notRated() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN

        ResponseEntity<RatingDto> result = webClient.get()
                .uri("/ratings/test-recipe-id-2")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntity(RatingDto.class)
                .block();

        //THEN
        RatingDto expected = RatingDto.builder()
                .recipeId("test-recipe-id-2")
                .rating(0)
                .build();

        assertThat(result, Matchers.notNullValue());
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(result.getBody(), Matchers.is(expected));
    }

    @Test
    void getOwnRatingFromRecipe_notLoggedIn() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN

        ResponseEntity<RatingDto> result = webClient.get()
                .uri("/ratings/test-recipe-id")
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.empty())
                .toEntity(RatingDto.class)
                .block();

        //THEN

        assertThat(result, Matchers.notNullValue());
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.FORBIDDEN));
    }

    @Test
    void addRating_newRating() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN
        CreateRatingDto ratingDto = CreateRatingDto.builder()
                .rating(3)
                .build();

        ResponseEntity<RatingDto> result = webClient.put()
                .uri("/ratings/test-recipe-id-2")
                .bodyValue(ratingDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntity(RatingDto.class)
                .block();

        //THEN

        RatingDto expected = RatingDto.builder()
                .recipeId("test-recipe-id-2")
                .rating(3)
                .build();

        assertThat(result, Matchers.notNullValue());
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(result.getBody(), Matchers.is(expected));
    }

    @Test
    void addRating_changeRating() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN
        CreateRatingDto ratingDto = CreateRatingDto.builder()
                .rating(5)
                .build();

        ResponseEntity<RatingDto> result = webClient.put()
                .uri("/ratings/test-recipe-id")
                .bodyValue(ratingDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .toEntity(RatingDto.class)
                .block();

        //THEN

        RatingDto expected = RatingDto.builder()
                .rating(5)
                .recipeId("test-recipe-id")
                .build();

        assertThat(result, Matchers.notNullValue());
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(result.getBody(), Matchers.is(expected));

    }

    @Test
    void addRating_RecipeDoesNotExcists() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN
        CreateRatingDto ratingDto = CreateRatingDto.builder()
                .rating(3)
                .build();

        ResponseEntity<RatingDto> result = webClient.put()
                .uri("/ratings/test-recipe-id-not-exists")
                .bodyValue(ratingDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.empty())
                .toEntity(RatingDto.class)
                .block();

        //THEN

        assertThat(result, Matchers.notNullValue());
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void addRating_notLoggedIn() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/api");

        //WHEN
        RatingDto ratingDto = RatingDto.builder()
                .rating(3)
                .recipeId("test-recipe-id-2")
                .build();

        ResponseEntity<RatingDto> result = webClient.post()
                .uri("/ratings/test-recipe-id-2")
                .bodyValue(ratingDto)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.empty())
                .toEntity(RatingDto.class)
                .block();

        //THEN

        assertThat(result, Matchers.notNullValue());
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.FORBIDDEN));

    }
}