package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.UserDoesNotExistsException;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.LoginJWTDto;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.UserLoginDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import com.github.flooooooooooorian.meinkochbuch.security.service.JwtUtilsService;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import com.github.flooooooooooorian.meinkochbuch.utils.TimeUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
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
    private IdUtils idUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtilsService jwtUtilsService;

    @Mock
    private TimeUtils timeUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<ChefUser> chefUserArgumentCaptor;

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

        assertThat(result, Matchers.is(LoginJWTDto.builder().jwt("test-jwt").authorities(Set.of()).build()));
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

        assertThrows(BadCredentialsException.class, () -> userService.login(userLoginDto));
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

    @Test
    void registerUser() {
        //GIVEN
        Instant instant = Instant.now();
        ChefUser expected = ChefUser.builder()
                .id("1")
                .joinedAt(instant)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .authorities(Set.of(ChefAuthorities.USER))
                .name("test-name")
                .password("test-password-encoded")
                .username("test-username")
                .enabled(false)
                .build();

        when(idUtils.generateId()).thenReturn("1");
        when(timeUtils.now()).thenReturn(instant);
        when(passwordEncoder.encode("test-password")).thenReturn("test-password-encoded");
        when(chefUserRepository.save(chefUserArgumentCaptor.capture())).thenReturn(expected);

        //WHEN
        ChefUser result = userService.registerUser("test-username", "test-name", "test-password");

        //THEN
        assertThat(result, Matchers.is(expected));
        assertThat(chefUserArgumentCaptor.getValue().getId(), Matchers.is(expected.getId()));
        assertThat(chefUserArgumentCaptor.getValue().getUsername(), Matchers.is(expected.getUsername()));
        assertThat(chefUserArgumentCaptor.getValue().getName(), Matchers.is(expected.getName()));
        assertThat(chefUserArgumentCaptor.getValue().getAuthorities(), Matchers.containsInAnyOrder(ChefAuthorities.USER));
        assertThat(chefUserArgumentCaptor.getValue().getJoinedAt(), Matchers.is(instant));
        assertThat(chefUserArgumentCaptor.getValue().isAccountNonExpired(), Matchers.is(true));
        assertThat(chefUserArgumentCaptor.getValue().isAccountNonLocked(), Matchers.is(true));
        assertThat(chefUserArgumentCaptor.getValue().isCredentialsNonExpired(), Matchers.is(true));
        assertThat(chefUserArgumentCaptor.getValue().isEnabled(), Matchers.is(false));
    }
}