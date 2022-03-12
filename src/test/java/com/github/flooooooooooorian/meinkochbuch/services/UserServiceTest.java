package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.UserDoesNotExistsException;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.LoginJWTDto;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.UserLoginDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import com.github.flooooooooooorian.meinkochbuch.security.service.JwtUtilsService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private ChefUserRepository chefUserRepository;

    @Mock
    private JwtUtilsService jwtUtilsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .cookbooks(List.of())
                .credentialsNonExpired(true)
                .favoriteRecipes(List.of())
                .enabled(true)
                .name("my-name")
                .recipes(List.of())
                .build();

        when(chefUserRepository.findChefUserById("1")).thenReturn(Optional.of(chefUser1));

        //WHEN

        ChefUser result = userService.getUserById("1");

        //THEN

        assertThat(result, Matchers.is(chefUser1));
    }

    @Test
    void getNonExistingUserById() {
        //GIVEN
        //WHEN
        //THEN
        assertThrows(UserDoesNotExistsException.class, () -> userService.getUserById("1"));
    }

    @Test
    void validLogin() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .credentialsNonExpired(true)
                .enabled(true)
                .name("my-name")
                .username("test-username")
                .password("test-password")
                .build();

        UserLoginDto userLoginDto = UserLoginDto.builder()
                .username("test-username")
                .password("test-password")
                .build();

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("name", chefUser1.getId());

        UsernamePasswordAuthenticationToken usernamePasswordData = new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword());
        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(chefUser1, chefUser1.getPassword());

        when(authenticationManager.authenticate(usernamePasswordData)).thenReturn(authenticationToken);
        when(jwtUtilsService.createToken(claims, chefUser1.getUsername())).thenReturn("test-jwt");

        //WHEN

        LoginJWTDto result = userService.login(userLoginDto);

        //THEN

        assertThat(result, Matchers.is(LoginJWTDto.builder().jwt("test-jwt").authorities(new String[0]).build()));
    }

    @Test
    void loginInvalidCredentials() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .credentialsNonExpired(true)
                .enabled(true)
                .name("my-name")
                .username("test-username")
                .password("test-password")
                .build();

        UserLoginDto userLoginDto = UserLoginDto.builder()
                .username("test-username")
                .password("test-wrong-password")
                .build();

        UsernamePasswordAuthenticationToken usernamePasswordData = new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword());

        when(authenticationManager.authenticate(usernamePasswordData)).thenThrow(new BadCredentialsException(""));

        //WHEN
        //THEN

        assertThrows(ResponseStatusException.class, () -> userService.login(userLoginDto));
    }

    @Test
    void loginNotEnabled() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .credentialsNonExpired(true)
                .enabled(false)
                .name("my-name")
                .username("test-username")
                .password("test-password")
                .build();

        UserLoginDto userLoginDto = UserLoginDto.builder()
                .username("test-username")
                .password("test-wrong-password")
                .build();


        UsernamePasswordAuthenticationToken usernamePasswordData = new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword());

        when(authenticationManager.authenticate(usernamePasswordData)).thenThrow(new DisabledException("Disabled"));

        //WHEN
        //THEN

        assertThrows(ResponseStatusException.class, () -> userService.login(userLoginDto));
    }
}