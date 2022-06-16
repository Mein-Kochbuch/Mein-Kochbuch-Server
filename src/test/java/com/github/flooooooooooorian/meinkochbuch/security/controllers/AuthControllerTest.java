package com.github.flooooooooooorian.meinkochbuch.security.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.LoginJWTDto;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.UserLoginDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest extends IntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void login_whenValidCredentials_thenReturnToken() {
        //GIVEN
        UserLoginDto credentials = UserLoginDto.builder()
                .username("some-user-email@email.de")
                .password("some-user-password")
                .build();

        //WHEN
        LoginJWTDto result = webClient.post()
                .uri("/auth/login")
                .bodyValue(credentials)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(LoginJWTDto.class)
                .returnResult()
                .getResponseBody();

        //THEN
        assertThat(result, notNullValue());
        assertThat(result.getAuthorities(), containsInAnyOrder(ChefAuthorities.USER));
        assertThat(result.getJwt(), notNullValue());
    }

    @Test
    void login_whenInvalidCredentials_thenThrow400() {
        //GIVEN
        UserLoginDto credentials = UserLoginDto.builder()
                .username("some-user-email@email.de")
                .password("some-user-wrong-password")
                .build();

        //WHEN
        webClient.post()
                .uri("/auth/login")
                .bodyValue(credentials)
                .exchange()
                .expectStatus()
                //THEN
                .isBadRequest();
    }
}