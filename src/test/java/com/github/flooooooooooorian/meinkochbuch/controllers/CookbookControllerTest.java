package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.controllers.responses.CookBookListResponse;
import com.github.flooooooooooorian.meinkochbuch.controllers.responses.ResponseInfo;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CookbookControllerTest extends IntegrationTest {
    @MockBean
    private IdUtils idUtils;

    @Autowired
    private WebTestClient webClient;

    @Test
    void getAllCookbooksAnonymous() {
        //GIVEN
        //WHEN
        CookBookListResponse result = webClient.get()
                .uri("/api/cookbooks")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(CookBookListResponse.class)
                .returnResult()
                .getResponseBody();

        //THEN
        CookbookPreviewDto expected1 = CookbookPreviewDto.builder()
                .id("test-cookbook-id-1")
                .name("test-cookbook-name-1")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-user-name")
                        .id("some-user-id")
                        .build())
                .ratingAverage(3)
                .build();

        CookbookPreviewDto expected2 = CookbookPreviewDto.builder()
                .id("test-cookbook-id-3")
                .name("test-cookbook-name-3")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-admin-name")
                        .id("some-admin-id")
                        .build())
                .ratingAverage(3)
                .build();

        ResponseInfo expectedInfo = ResponseInfo.builder()
                .pages(0)
                .count(2)
                .next(null)
                .prev(null)
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getInfo(), is(expectedInfo));
        assertThat(result.getResults(), containsInAnyOrder(expected1, expected2));
    }

    @Test
    void getAllCookbooksOwnAndPublic() {
        //GIVEN
        //WHEN
        CookBookListResponse result = webClient.get()
                .uri("/api/cookbooks")
                .header("Authorization", "Bearer " + getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(CookBookListResponse.class)
                .returnResult()
                .getResponseBody();

        //THEN
        CookbookPreviewDto expected1 = CookbookPreviewDto.builder()
                .id("test-cookbook-id-1")
                .name("test-cookbook-name-1")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-user-name")
                        .id("some-user-id")
                        .build())
                .ratingAverage(3)
                .build();

        CookbookPreviewDto expected2 = CookbookPreviewDto.builder()
                .id("test-cookbook-id-3")
                .name("test-cookbook-name-3")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-admin-name")
                        .id("some-admin-id")
                        .build())
                .ratingAverage(3)
                .build();

        CookbookPreviewDto expected3 = CookbookPreviewDto.builder()
                .id("test-cookbook-id-2")
                .name("test-cookbook-name-2")
                .owner(ChefUserPreviewDto.builder()
                        .name("some-user-name")
                        .id("some-user-id")
                        .build())
                .ratingAverage(3)
                .build();

        assertThat(result, notNullValue());
        assertThat(result.getResults(), containsInAnyOrder(expected1, expected2, expected3));
    }

    @Test
    void getCookbookByIdAnonymousPublic() {
        //GIVEN
        //WHEN
        CookbookDto result = webClient.get()
                .uri("/api/cookbooks/test-cookbook-id-1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(CookbookDto.class)
                .returnResult()
                .getResponseBody();

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
                        .name("test-recipe-name-A")
                        .ratingAverage(3)
                        .ratingCount(1)
                        .build()))
                .ratingAverage(3)
                .thumbnail(null)
                .build();

        assertThat(result, is(expected));
    }

    @Test
    void getCookbookByIdAnonymousPrivate() {
        //GIVEN
        //WHEN
        webClient.get()
                .uri("/api/cookbooks/test-cookbook-id-2")
                .exchange()
                .expectStatus()
                //THEN
                .isForbidden();
    }

    @Test
    void getCookbookByIdNotExisting() {
        //GIVEN
        //WHEN
        webClient.get()
                .uri("/api/cookbooks/test-cookbook-id-not-found")
                .exchange()
                .expectStatus()
                //THEN
                .isNotFound();
    }

    @Test
    void createCookbookOnlyPublic() {
        //GIVEN
        when(idUtils.generateId()).thenReturn("test-cookbook-id");

        CookbookCreationDto cookbookCreationDto = CookbookCreationDto.builder()
                .privacy(false)
                .name("test-cookbook-name")
                .recipeIds(List.of("test-recipe-id-1", "test-recipe-id-3"))
                .build();

        //WHEN
        CookbookDto actual = webClient.post()
                .uri("/api/cookbooks")
                .header(HttpHeaders.AUTHORIZATION, getTokenByUserId("some-user-id"))
                .bodyValue(cookbookCreationDto)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(CookbookDto.class)
                .returnResult()
                .getResponseBody();

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
                                .name("test-recipe-name-A")
                                .ratingCount(1)
                                .ratingAverage(3)
                                .owner(ChefUserPreviewDto.builder()
                                        .name("some-user-name")
                                        .id("some-user-id")
                                        .build())
                                .build(),
                        RecipePreviewDto.builder()
                                .id("test-recipe-id-3")
                                .name("test-recipe-name-C")
                                .ratingCount(2)
                                .ratingAverage(4.5)
                                .owner(ChefUserPreviewDto.builder()
                                        .name("some-admin-name")
                                        .id("some-admin-id")
                                        .build())
                                .build()))
                .privacy(false)
                .ratingAverage(4)
                .build();

        assertThat(actual, is(expected));
    }

    @Test
    void createCookbookWithPrivacy() {
        //GIVEN
        when(idUtils.generateId()).thenReturn("test-cookbook-id");

        CookbookCreationDto cookbookCreationDto = CookbookCreationDto.builder()
                .privacy(false)
                .name("test-cookbook-name")
                .recipeIds(List.of("test-recipe-id-1", "test-recipe-id-2"))
                .build();

        //WHEN
        CookbookDto actual = webClient.post()
                .uri("/api/cookbooks")
                .header(HttpHeaders.AUTHORIZATION, getTokenByUserId("some-user-id"))
                .bodyValue(cookbookCreationDto)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(CookbookDto.class)
                .returnResult()
                .getResponseBody();

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
                                .name("test-recipe-name-A")
                                .ratingCount(1)
                                .ratingAverage(3)
                                .owner(ChefUserPreviewDto.builder()
                                        .name("some-user-name")
                                        .id("some-user-id")
                                        .build())
                                .build(),
                        RecipePreviewDto.builder()
                                .id("test-recipe-id-2")
                                .name("test-recipe-name-B")
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

    @Test
    void editCookbook() {
        //GIVEN


        CookbookCreationDto cookbookCreationDto = CookbookCreationDto.builder()
                .name("test-cookbook-name-1-new")
                .privacy(true)
                .recipeIds(List.of("test-recipe-id-3"))
                .build();
        //WHEN
        CookbookDto actual = webClient.put()
                .uri("/api/cookbooks/test-cookbook-id-1")
                .bodyValue(cookbookCreationDto)
                .header(HttpHeaders.AUTHORIZATION, getTokenByUserId("some-user-id"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(CookbookDto.class)
                .returnResult()
                .getResponseBody();

        //THEN
        CookbookDto expected = CookbookDto.builder()
                .id("test-cookbook-id-1")
                .name(cookbookCreationDto.getName())
                .owner(ChefUserPreviewDto.builder()
                        .id("some-user-id")
                        .name("some-user-name")
                        .build())
                .ratingAverage(4.5)
                .privacy(true)
                .recipes(List.of(RecipePreviewDto.builder()
                        .id("test-recipe-id-3")
                        .name("test-recipe-name-C")
                        .ratingCount(2)
                        .ratingAverage(4.5)
                        .owner(ChefUserPreviewDto.builder()
                                .name("some-admin-name")
                                .id("some-admin-id")
                                .build())
                        .build()))
                .build();

        assertThat(actual, is(expected));
    }

    @Test
    void editCookbookAccessDenied() {
        //GIVEN
        CookbookCreationDto cookbookCreationDto = CookbookCreationDto.builder()
                .name("test-cookbook-name-1-new")
                .privacy(true)
                .recipeIds(List.of("test-recipe-id-3"))
                .build();
        //WHEN
        webClient.put()
                .uri("/api/cookbooks/test-cookbook-id-1")
                .bodyValue(cookbookCreationDto)
                .header(HttpHeaders.AUTHORIZATION, getTokenByUserId("some-admin-id"))
                .exchange()
                .expectStatus()
                //THEN
                .isForbidden();
    }
}
