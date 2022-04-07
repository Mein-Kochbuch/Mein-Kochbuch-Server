package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.CookPrivacyForbiddenException;
import com.github.flooooooooooorian.meinkochbuch.exceptions.CookbookDoesNotExist;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.CookbookContent;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.repository.CookbookRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CookbookServiceTest {

    private final CookbookRepository cookbookRepository = mock(CookbookRepository.class);

    private final CookbookService cookbookService = new CookbookService(cookbookRepository);

    @Test
    void findAllCookbooksWithOutUser() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("test-user-id")
                .name("test-user-name")
                .build();

        Recipe r1 = Recipe.builder()
                .id("1")
                .owner(chefUser1)
                .privacy(false)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        CookbookContent cookbookContent = CookbookContent.builder()
                .cookbook(Cookbook.ofId("test-cookbook-id"))
                .recipe(r1)
                .build();

        Cookbook expected = Cookbook.builder()
                .id("test-cookbook-id")
                .name("test-cookbook-name")
                .privacy(false)
                .owner(chefUser1)
                .contents(List.of(cookbookContent))
                .build();


        when(cookbookRepository.findAllByPrivacyIsFalseOrOwner_Id(null)).thenReturn(List.of(expected));
        //WHEN

        List<Cookbook> result = cookbookService.findAllCookbooks(null);

        //THEN
        assertThat(result, containsInAnyOrder(expected));
    }

    @Test
    void getAllCookbooksAndOwnCookbooks() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id("test-user-id")
                .name("test-user-name")
                .build();

        ChefUser chefUser2 = ChefUser.builder()
                .id("test-user-id-2")
                .name("test-user-name-2")
                .build();


        Recipe r1 = Recipe.builder()
                .id("1")
                .owner(chefUser1)
                .privacy(false)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        CookbookContent cookbookContent = CookbookContent.builder()
                .cookbook(Cookbook.ofId("test-cookbook-id"))
                .recipe(r1)
                .build();

        Cookbook expected = Cookbook.builder()
                .id("test-cookbook-id")
                .name("test-cookbook-name")
                .privacy(false)
                .owner(chefUser1)
                .contents(List.of(cookbookContent))
                .build();

        CookbookContent cookbookContent2 = CookbookContent.builder()
                .cookbook(Cookbook.ofId("test-cookbook-id"))
                .recipe(r1)
                .build();

        Cookbook expected2 = Cookbook.builder()
                .id("test-cookbook-id-2")
                .name("test-cookbook-name-2")
                .privacy(true)
                .owner(chefUser2)
                .contents(List.of(cookbookContent2))
                .build();


        when(cookbookRepository.findAllByPrivacyIsFalseOrOwner_Id(chefUser1.getId())).thenReturn(List.of(expected, expected2));
        //WHEN

        List<Cookbook> result = cookbookService.findAllCookbooks(chefUser1.getId());

        //THEN
        assertThat(result, containsInAnyOrder(expected, expected2));
    }

    @Test
    void findCookbookById() {
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

        Cookbook cookbook = Cookbook.builder()
                .id("1")
                .owner(chefUser1)
                .privacy(false)
                .name("test-cookbook-name")
                .build();

        when(cookbookRepository.findById("1")).thenReturn(Optional.ofNullable(cookbook));

        //WHEN

        Cookbook result = cookbookService.findCookbookById("1", Optional.empty());

        //THEN

        assertThat(result, Matchers.is(cookbook));
    }

    @Test
    void findCookbookByIdAnonymousDenied() {
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

        Cookbook cookbook = Cookbook.builder()
                .id("1")
                .owner(chefUser1)
                .privacy(true)
                .name("test-cookbook-name")
                .build();

        when(cookbookRepository.findById("1")).thenReturn(Optional.ofNullable(cookbook));

        //WHEN
        //THEN

        assertThrows(CookPrivacyForbiddenException.class, () -> cookbookService.findCookbookById("1", Optional.empty()));
    }

    @Test
    void findCookbookByIdNonExisting() {
        //GIVEN

        when(cookbookRepository.findById("1")).thenReturn(Optional.empty());

        //WHEN
        //THEN

        assertThrows(CookbookDoesNotExist.class, () -> cookbookService.findCookbookById("1", Optional.empty()));
    }

    @Test
    void findCookbookByIdUserAllowed() {
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

        Cookbook cookbook = Cookbook.builder()
                .id("1")
                .owner(chefUser1)
                .privacy(true)
                .name("test-cookbook-name")
                .build();

        when(cookbookRepository.findById("1")).thenReturn(Optional.ofNullable(cookbook));

        //WHEN

        Cookbook result = cookbookService.findCookbookById("1", Optional.of(chefUser1.getId()));

        //THEN

        assertThat(result, Matchers.is(cookbook));
    }

}