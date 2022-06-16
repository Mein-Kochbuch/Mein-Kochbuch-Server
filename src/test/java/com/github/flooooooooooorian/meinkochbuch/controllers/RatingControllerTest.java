package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.dtos.rating.CreateRatingDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.rating.RatingDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RatingControllerTest extends IntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void getOwnRatingFromRecipe_valid() {
        //GIVEN
        //WHEN

        RatingDto result = webClient.get()
                .uri("/api/ratings/test-recipe-id-1")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RatingDto.class)
                .returnResult()
                .getResponseBody();

        //THEN
        RatingDto expected = RatingDto.builder()
                .recipeId("test-recipe-id-1")
                .rating(3)
                .build();

        assertThat(result, Matchers.is(expected));
    }

    @Test
    void getOwnRatingFromRecipe_notRated() {
        //GIVEN
        //WHEN

        RatingDto result = webClient.get()
                .uri("/api/ratings/test-recipe-id-2")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RatingDto.class)
                .returnResult()
                .getResponseBody();

        //THEN
        RatingDto expected = RatingDto.builder()
                .recipeId("test-recipe-id-2")
                .rating(0)
                .build();

        assertThat(result, Matchers.is(expected));
    }

    @Test
    void getOwnRatingFromRecipe_notLoggedIn() {
        //GIVEN
        //WHEN

        webClient.get()
                .uri("/api/ratings/test-recipe-id-1")
                .exchange()
                .expectStatus()
                //THEN
                .isForbidden();
    }

    @Test
    void addRating_newRating() {
        //GIVEN
        //WHEN
        CreateRatingDto ratingDto = CreateRatingDto.builder()
                .rating(3)
                .build();

        RatingDto result = webClient.put()
                .uri("/api/ratings/test-recipe-id-2")
                .bodyValue(ratingDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RatingDto.class)
                .returnResult()
                .getResponseBody();

        //THEN
        RatingDto expected = RatingDto.builder()
                .recipeId("test-recipe-id-2")
                .rating(3)
                .build();

        assertThat(result, Matchers.is(expected));
    }

    @Test
    void addRating_changeRating() {
        //GIVEN
        //WHEN
        CreateRatingDto ratingDto = CreateRatingDto.builder()
                .rating(5)
                .build();

        RatingDto result = webClient.put()
                .uri("/api/ratings/test-recipe-id-1")
                .bodyValue(ratingDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RatingDto.class)
                .returnResult()
                .getResponseBody();

        //THEN
        RatingDto expected = RatingDto.builder()
                .rating(5)
                .recipeId("test-recipe-id-1")
                .build();

        assertThat(result, Matchers.is(expected));
    }

    @Test
    void addRating_RecipeDoesNotExcists() {
        //GIVEN
        //WHEN
        CreateRatingDto ratingDto = CreateRatingDto.builder()
                .rating(3)
                .build();

        webClient.put()
                .uri("/api/ratings/test-recipe-id-not-exists")
                .bodyValue(ratingDto)
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                //THEN
                .isBadRequest();
    }

    @Test
    void addRating_notLoggedIn() {
        //GIVEN
        //WHEN
        RatingDto ratingDto = RatingDto.builder()
                .rating(3)
                .recipeId("test-recipe-id-2")
                .build();

        webClient.post()
                .uri("/api/ratings/test-recipe-id-2")
                .bodyValue(ratingDto)
                .exchange()
                .expectStatus()
                //THEN
                .isForbidden();
    }
}