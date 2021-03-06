package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.UserDoesNotExistsException;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void getUserByIdOwn() {
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
    void getUserByIdOther() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .cookbooks(List.of(Cookbook.ofId("1"),
                        Cookbook.builder()
                                .id("2")
                                .privacy(true)
                        .build()))
                .credentialsNonExpired(true)
                .favoriteRecipes(List.of())
                .enabled(true)
                .name("my-name")
                .recipes(List.of(
                        Recipe.ofId("1"),
                        Recipe.builder()
                                .id("2")
                                .privacy(true)
                                .build()))
                .build();

        when(chefUserRepository.findChefUserById("1")).thenReturn(Optional.of(chefUser1));

        //WHEN

        ChefUser result = userService.getUserById("1", Optional.of("2"));

        //THEN

        ChefUser expected = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .cookbooks(List.of(Cookbook.ofId("1")))
                .credentialsNonExpired(true)
                .favoriteRecipes(List.of())
                .enabled(true)
                .name("my-name")
                .recipes(List.of(Recipe.ofId("1")))
                .build();

        assertThat(result.getId(), Matchers.is(expected.getId()));
        assertThat(result.getName(), Matchers.is(expected.getName()));
        assertThat(result.getCookbooks(), Matchers.is(expected.getCookbooks()));
        assertThat(result.getRecipes(), Matchers.is(expected.getRecipes()));
    }

    @Test
    void getUserByIdAnonymousOther() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .cookbooks(List.of(Cookbook.ofId("1"),
                        Cookbook.builder()
                                .id("2")
                                .privacy(true)
                                .build()))
                .credentialsNonExpired(true)
                .favoriteRecipes(List.of())
                .enabled(true)
                .name("my-name")
                .recipes(List.of(
                        Recipe.ofId("1"),
                        Recipe.builder()
                                .id("2")
                                .privacy(true)
                                .build()))
                .build();

        when(chefUserRepository.findChefUserById("1")).thenReturn(Optional.of(chefUser1));

        //WHEN

        ChefUser result = userService.getUserById("1", Optional.empty());

        //THEN

        ChefUser expected = ChefUser.builder()
                .id("1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of())
                .cookbooks(List.of(Cookbook.ofId("1")))
                .credentialsNonExpired(true)
                .favoriteRecipes(List.of())
                .enabled(true)
                .name("my-name")
                .recipes(List.of(Recipe.ofId("1")))
                .build();

        assertThat(result.getId(), Matchers.is(expected.getId()));
        assertThat(result.getName(), Matchers.is(expected.getName()));
        assertThat(result.getCookbooks(), Matchers.is(expected.getCookbooks()));
        assertThat(result.getRecipes(), Matchers.is(expected.getRecipes()));
    }

    @Test
    void getNonExistingUserById() {
        //GIVEN
        //WHEN
        //THEN
        assertThrows(UserDoesNotExistsException.class, () -> userService.getUserById("1", Optional.of("1")));
    }
}
