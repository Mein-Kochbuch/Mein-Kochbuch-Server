package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserProfileDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreview;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest extends IntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getUserProfileByIdOwn() {
        //GIVEN

        //WHEN
        ChefUserProfileDto actual = webTestClient.get()
                .uri("/api/user/some-user-id")
                .header(HttpHeaders.AUTHORIZATION, getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(ChefUserProfileDto.class)
                .returnResult()
                .getResponseBody();

        //THEN
        ChefUserProfileDto expected = ChefUserProfileDto.builder()
                .id("some-user-id")
                .cookbooks(List.of(CookbookPreview.builder()
                                .id("test-cookbook-id-1")
                                .name("test-cookbook-name-1")
                                .owner(ChefUserPreviewDto.builder()
                                        .id("some-user-id")
                                        .name("some-user-name")
                                        .build())
                                .ratingAverage(3)
                                .build(),
                        CookbookPreview.builder()
                                .id("test-cookbook-id-2")
                                .name("test-cookbook-name-2")
                                .owner(ChefUserPreviewDto.builder()
                                        .id("some-user-id")
                                        .name("some-user-name")
                                        .build())
                                .ratingAverage(3)
                                .build()))
                .name("some-user-name")
                .recipes(List.of(RecipePreviewDto.builder()
                                .id("test-recipe-id-2")
                                .name("test-recipe-name-B")
                                .owner(ChefUserPreviewDto.builder()
                                        .id("some-user-id")
                                        .name("some-user-name")
                                        .build())
                                .build(),
                        RecipePreviewDto.builder()
                                .id("test-recipe-id-1")
                                .name("test-recipe-name-A")
                                .owner(ChefUserPreviewDto.builder()
                                        .id("some-user-id")
                                        .name("some-user-name")
                                        .build())
                                .ratingAverage(3)
                                .ratingCount(1)
                                .build()
                ))
                .build();

        assertThat(actual, is(expected));
    }

    @Test
    void getUserProfileByIdOther() {
        //GIVEN

        //WHEN
        ChefUserProfileDto actual = webTestClient.get()
                .uri("/api/user/some-admin-id")
                .header(HttpHeaders.AUTHORIZATION, getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(ChefUserProfileDto.class)
                .returnResult()
                .getResponseBody();

        //THEN

        ChefUserProfileDto expected = ChefUserProfileDto.builder()
                .id("some-admin-id")
                .cookbooks(List.of(CookbookPreview.builder()
                        .id("test-cookbook-id-3")
                        .name("test-cookbook-name-3")
                        .owner(ChefUserPreviewDto.builder()
                                .id("some-admin-id")
                                .name("some-admin-name")
                                .build())
                        .ratingAverage(3)
                        .build()))
                .name("some-admin-name")
                .recipes(List.of(RecipePreviewDto.builder()
                        .id("test-recipe-id-3")
                        .name("test-recipe-name-C")
                        .owner(ChefUserPreviewDto.builder()
                                .id("some-admin-id")
                                .name("some-admin-name")
                                .build())
                        .ratingAverage(4.5)
                        .ratingCount(2)
                        .build()))
                .build();

        assertThat(actual, is(expected));
    }

    @Test
    void getUserProfileByIdAnonymousOther() {
        //GIVEN

        //WHEN
        ChefUserProfileDto actual = webTestClient.get()
                .uri("/api/user/some-admin-id")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(ChefUserProfileDto.class)
                .returnResult()
                .getResponseBody();

        //THEN

        ChefUserProfileDto expected = ChefUserProfileDto.builder()
                .id("some-admin-id")
                .cookbooks(List.of(CookbookPreview.builder()
                        .id("test-cookbook-id-3")
                        .name("test-cookbook-name-3")
                        .owner(ChefUserPreviewDto.builder()
                                .id("some-admin-id")
                                .name("some-admin-name")
                                .build())
                        .ratingAverage(3)
                        .build()))
                .name("some-admin-name")
                .recipes(List.of(RecipePreviewDto.builder()
                        .id("test-recipe-id-3")
                        .name("test-recipe-name-C")
                        .owner(ChefUserPreviewDto.builder()
                                .id("some-admin-id")
                                .name("some-admin-name")
                                .build())
                        .ratingAverage(4.5)
                        .ratingCount(2)
                        .build()))
                .build();

        assertThat(actual, is(expected));
    }
}
