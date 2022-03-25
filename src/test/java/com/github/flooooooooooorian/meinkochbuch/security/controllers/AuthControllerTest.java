package com.github.flooooooooooorian.meinkochbuch.security.controllers;

import com.github.flooooooooooorian.meinkochbuch.IntegrationTest;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.LoginJWTDto;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.UserLoginDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest extends IntegrationTest {

    @LocalServerPort
    private int port;

    private WebClient webClient;

    @Test
    void login_whenValidCredentials_thenReturnToken() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/auth");
        UserLoginDto credentials = UserLoginDto.builder()
                .username("some-user-email@email.de")
                .password("some-user-password")
                .build();

        //WHEN

        ResponseEntity<LoginJWTDto> result = webClient.post()
                .uri("/login")
                .bodyValue(credentials)
                .retrieve()
                .toEntity(LoginJWTDto.class)
                .block();

        //THEN

        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getBody().getAuthorities(), containsInAnyOrder(ChefAuthorities.USER));
        assertThat(result.getBody().getJwt(), notNullValue());

    }

    @Test
    void login_whenInvalidCredentials_thenThrow400() {
        //GIVEN
        webClient = WebClient.create("http://localhost:" + port + "/auth");
        UserLoginDto credentials = UserLoginDto.builder()
                .username("some-user-email@email.de")
                .password("some-user-wrong-password")
                .build();

        //WHEN

        ResponseEntity<LoginJWTDto> result = webClient.post()
                .uri("/login")
                .bodyValue(credentials)
                .retrieve()
                .onStatus(HttpStatus::isError, e -> Mono.empty())
                .toEntity(LoginJWTDto.class)
                .block();

        //THEN

        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), Matchers.is(HttpStatus.BAD_REQUEST));
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getBody().getJwt(), nullValue());
        assertThat(result.getBody().getAuthorities(), nullValue());
    }
}