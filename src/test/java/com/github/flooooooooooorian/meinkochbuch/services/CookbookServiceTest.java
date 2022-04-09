package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookCreationDto;
import com.github.flooooooooooorian.meinkochbuch.exceptions.CookPrivacyForbiddenException;
import com.github.flooooooooooorian.meinkochbuch.exceptions.CookbookDoesNotExist;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.CookbookContent;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.repository.CookbookRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.access.AccessDeniedException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CookbookServiceTest {

    private final CookbookRepository cookbookRepository = mock(CookbookRepository.class);
    private final RecipeService recipeService = mock(RecipeService.class);
    private final IdUtils idUtils = mock(IdUtils.class);

    private final CookbookService cookbookService = new CookbookService(cookbookRepository, recipeService, idUtils);

    private final ArgumentCaptor<Cookbook> argumentCaptor = ArgumentCaptor.forClass(Cookbook.class);

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

    @Test
    void addCookbook() {
        //GIVEN
        CookbookCreationDto cookbookCreationDto = CookbookCreationDto.builder()
                .name("test-name")
                .privacy(false)
                .recipeIds(List.of("test-recipe-id-1"))
                .build();

        Cookbook cookbook = Cookbook.builder()
                .id("test-id")
                .owner(ChefUser.ofId("test-user-id"))
                .name("test-name")
                .privacy(false)
                .thumbnail(null)
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.ofId("test-recipe-id-1"))
                        .cookbook(Cookbook.ofId("test-id"))
                        .build()))
                .build();

        when(cookbookRepository.save(argumentCaptor.capture())).thenReturn(cookbook);
        when(recipeService.getAllRecipesByIds(cookbookCreationDto.getRecipeIds())).thenReturn(cookbook.getContents().stream()
                .map(CookbookContent::getRecipe)
                .toList());
        when(idUtils.generateId()).thenReturn("test-id");

        //WHEN

        Cookbook actual = cookbookService.addCookbook(cookbookCreationDto, "test-user-id");

        //THEN

        assertThat(actual, is(cookbook));
        assertThat(argumentCaptor.getValue().getId(), is("test-id"));
        assertThat(argumentCaptor.getValue().getName(), is("test-name"));
        assertThat(argumentCaptor.getValue().getOwner().getId(), is("test-user-id"));
        assertThat(argumentCaptor.getValue().isPrivacy(), is(false));
        assertThat(argumentCaptor.getValue().getThumbnail(), nullValue());
        assertThat(argumentCaptor.getValue().getContents().size(), is(1));
        assertThat(argumentCaptor.getValue().getContents().get(0).getRecipe().getId(), is("test-recipe-id-1"));
        assertThat(argumentCaptor.getValue().getContents().get(0).getCookbook().getId(), is("test-id"));
        assertThat(actual.getAverageRating(), is(0.0));
    }

    @Test
    void addCookbookPrivacy() {
        //GIVEN
        CookbookCreationDto cookbookCreationDto = CookbookCreationDto.builder()
                .name("test-name")
                .privacy(true)
                .recipeIds(List.of("test-recipe-id-1"))
                .build();

        Cookbook cookbook = Cookbook.builder()
                .id("test-id")
                .owner(ChefUser.ofId("test-user-id"))
                .name("test-name")
                .privacy(true)
                .thumbnail(null)
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.ofId("test-recipe-id-1"))
                        .cookbook(Cookbook.ofId("test-id"))
                        .build()))
                .build();

        when(cookbookRepository.save(argumentCaptor.capture())).thenReturn(cookbook);
        when(recipeService.getAllRecipesByIds(cookbookCreationDto.getRecipeIds())).thenReturn(cookbook.getContents().stream()
                .map(CookbookContent::getRecipe)
                .toList());
        when(idUtils.generateId()).thenReturn("test-id");

        //WHEN

        Cookbook actual = cookbookService.addCookbook(cookbookCreationDto, "test-user-id");

        //THEN

        assertThat(actual, is(cookbook));
        assertThat(argumentCaptor.getValue().getId(), is("test-id"));
        assertThat(argumentCaptor.getValue().getName(), is("test-name"));
        assertThat(argumentCaptor.getValue().getOwner().getId(), is("test-user-id"));
        assertThat(argumentCaptor.getValue().isPrivacy(), is(true));
        assertThat(argumentCaptor.getValue().getThumbnail(), nullValue());
        assertThat(argumentCaptor.getValue().getContents().size(), is(1));
        assertThat(argumentCaptor.getValue().getContents().get(0).getRecipe().getId(), is("test-recipe-id-1"));
        assertThat(argumentCaptor.getValue().getContents().get(0).getCookbook().getId(), is("test-id"));
        assertThat(actual.getAverageRating(), is(0.0));
    }

    @Test
    void addCookbookWithPrivateRecipes() {
        //GIVEN
        CookbookCreationDto cookbookCreationDto = CookbookCreationDto.builder()
                .name("test-name")
                .privacy(false)
                .recipeIds(List.of("test-recipe-id-1"))
                .build();

        Cookbook cookbook = Cookbook.builder()
                .id("test-id")
                .owner(ChefUser.ofId("test-user-id"))
                .name("test-name")
                .privacy(true)
                .thumbnail(null)
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.builder()
                                .id("test-recipe-id-1")
                                .privacy(true)
                                .build())
                        .cookbook(Cookbook.ofId("test-id"))
                        .build()))
                .build();

        when(cookbookRepository.save(argumentCaptor.capture())).thenReturn(cookbook);
        when(recipeService.getAllRecipesByIds(cookbookCreationDto.getRecipeIds())).thenReturn(cookbook.getContents().stream()
                .map(CookbookContent::getRecipe)
                .toList());
        when(idUtils.generateId()).thenReturn("test-id");

        //WHEN

        Cookbook actual = cookbookService.addCookbook(cookbookCreationDto, "test-user-id");

        //THEN

        assertThat(actual, is(cookbook));
        assertThat(argumentCaptor.getValue().getId(), is("test-id"));
        assertThat(argumentCaptor.getValue().getName(), is("test-name"));
        assertThat(argumentCaptor.getValue().getOwner().getId(), is("test-user-id"));
        assertThat(argumentCaptor.getValue().isPrivacy(), is(true));
        assertThat(argumentCaptor.getValue().getThumbnail(), nullValue());
        assertThat(argumentCaptor.getValue().getContents().size(), is(1));
        assertThat(argumentCaptor.getValue().getContents().get(0).getRecipe().getId(), is("test-recipe-id-1"));
        assertThat(argumentCaptor.getValue().getContents().get(0).getCookbook().getId(), is("test-id"));
        assertThat(actual.getAverageRating(), is(0.0));
    }

    @Test
    void editCookbookById() {
        //GIVEN
        ChefUser chefUser = ChefUser.ofId("test-user-id");
        Cookbook cookbookToChange = Cookbook.builder()
                .id("test-cookbook-id-1")
                .name("test-cookbook-name")
                .privacy(false)
                .owner(chefUser)
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.ofId("test-recipe-id"))
                        .cookbook(Cookbook.ofId("test-cookbook-id"))
                        .build()))
                .build();
        CookbookCreationDto cookbookCreationDto = CookbookCreationDto.builder()
                .name("test-cookbook-name-new")
                .privacy(true)
                .recipeIds(List.of("test-recipe-id-2"))
                .build();

        Cookbook expected = Cookbook.builder()
                .id("test-cookbook-id-1")
                .owner(chefUser)
                .name("test-cookbook-name-new")
                .privacy(true)
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.ofId("test-recipe-id-2"))
                        .cookbook(Cookbook.ofId("test-cookbook-id"))
                        .build()))
                .build();

        when(cookbookRepository.findById("test-cookbook-id-1")).thenReturn(Optional.ofNullable(cookbookToChange));
        when(recipeService.getAllRecipesByIds(List.of("test-recipe-id-2"))).thenReturn(List.of(Recipe.ofId("test-recipe-id-2")));
        when(cookbookRepository.save(argumentCaptor.capture())).thenReturn(expected);

        //WHEN
        Cookbook actual = cookbookService.editCookbookById("test-cookbook-id-1", cookbookCreationDto, "test-user-id");

        //THEN

        assertThat(actual, is(expected));

        assertThat(argumentCaptor.getValue().getId(), is(expected.getId()));
        assertThat(argumentCaptor.getValue().getName(), is(expected.getName()));
        assertThat(argumentCaptor.getValue().getOwner(), is(expected.getOwner()));
        assertThat(argumentCaptor.getValue().isPrivacy(), is(expected.isPrivacy()));
        assertThat(argumentCaptor.getValue().getAverageRating(), is(expected.getAverageRating()));
        assertThat(argumentCaptor.getValue().getThumbnail(), is(expected.getThumbnail()));
        assertThat(argumentCaptor.getValue().getContents().size(), is(expected.getContents().size()));
        assertThat(argumentCaptor.getValue().getContents().get(0).getRecipe().getId(), is(expected.getContents().get(0).getRecipe().getId()));
    }

    @Test
    void editCookbookByIdWithPrivateRecipes() {
        //GIVEN
        ChefUser chefUser = ChefUser.ofId("test-user-id");
        Cookbook cookbookToChange = Cookbook.builder()
                .id("test-cookbook-id-1")
                .name("test-cookbook-name")
                .privacy(false)
                .owner(chefUser)
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.ofId("test-recipe-id"))
                        .cookbook(Cookbook.ofId("test-cookbook-id"))
                        .build()))
                .build();
        CookbookCreationDto cookbookCreationDto = CookbookCreationDto.builder()
                .name("test-cookbook-name-new")
                .privacy(false)
                .recipeIds(List.of("test-recipe-id-2"))
                .build();

        Cookbook expected = Cookbook.builder()
                .id("test-cookbook-id-1")
                .owner(chefUser)
                .name("test-cookbook-name-new")
                .privacy(true)
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.ofId("test-recipe-id-2"))
                        .cookbook(Cookbook.ofId("test-cookbook-id"))
                        .build()))
                .build();

        when(cookbookRepository.findById("test-cookbook-id-1")).thenReturn(Optional.ofNullable(cookbookToChange));
        when(recipeService.getAllRecipesByIds(List.of("test-recipe-id-2"))).thenReturn(List.of(Recipe.builder()
                .id("test-recipe-id-2")
                .privacy(true)
                .build()));
        when(cookbookRepository.save(argumentCaptor.capture())).thenReturn(expected);

        //WHEN
        Cookbook actual = cookbookService.editCookbookById("test-cookbook-id-1", cookbookCreationDto, "test-user-id");

        //THEN

        assertThat(actual, is(expected));

        assertThat(argumentCaptor.getValue().getId(), is(expected.getId()));
        assertThat(argumentCaptor.getValue().getName(), is(expected.getName()));
        assertThat(argumentCaptor.getValue().getOwner(), is(expected.getOwner()));
        assertThat(argumentCaptor.getValue().isPrivacy(), is(expected.isPrivacy()));
        assertThat(argumentCaptor.getValue().getAverageRating(), is(expected.getAverageRating()));
        assertThat(argumentCaptor.getValue().getThumbnail(), is(expected.getThumbnail()));
        assertThat(argumentCaptor.getValue().getContents().size(), is(expected.getContents().size()));
        assertThat(argumentCaptor.getValue().getContents().get(0).getRecipe().getId(), is(expected.getContents().get(0).getRecipe().getId()));
    }

    @Test
    void editCookbookByIdAccessDenied() {
        //GIVEN
        ChefUser chefUser = ChefUser.ofId("test-user-id");
        Cookbook cookbookToChange = Cookbook.builder()
                .id("test-cookbook-id-1")
                .name("test-cookbook-name")
                .privacy(false)
                .owner(chefUser)
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.ofId("test-recipe-id"))
                        .cookbook(Cookbook.ofId("test-cookbook-id"))
                        .build()))
                .build();
        CookbookCreationDto cookbookCreationDto = CookbookCreationDto.builder()
                .name("test-cookbook-name-new")
                .privacy(true)
                .recipeIds(List.of("test-recipe-id-2"))
                .build();

        Cookbook expected = Cookbook.builder()
                .id("test-cookbook-id-1")
                .owner(chefUser)
                .name("test-cookbook-name-new")
                .privacy(true)
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.ofId("test-recipe-id-2"))
                        .cookbook(Cookbook.ofId("test-cookbook-id"))
                        .build()))
                .build();

        when(cookbookRepository.findById("test-cookbook-id-1")).thenReturn(Optional.ofNullable(cookbookToChange));
        when(recipeService.getAllRecipesByIds(List.of("test-recipe-id-2"))).thenReturn(List.of(Recipe.ofId("test-recipe-id-2")));
        when(cookbookRepository.save(argumentCaptor.capture())).thenReturn(expected);

        //WHEN
        //THEN
        assertThrows(AccessDeniedException.class, () -> cookbookService.editCookbookById("test-cookbook-id-1", cookbookCreationDto, "test-user-id-other"));
    }
}
