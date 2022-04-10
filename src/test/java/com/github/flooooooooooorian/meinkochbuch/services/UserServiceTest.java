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

        ChefUser result = userService.getUserById("1", Optional.of("1"));

        //THEN

        assertThat(result, Matchers.is(chefUser1));
    }

    @Test
    void getNonExistingUserById() {
        //GIVEN
        //WHEN
        //THEN
        assertThrows(UserDoesNotExistsException.class, () -> userService.getUserById("1", Optional.of("1")));
    }
}
