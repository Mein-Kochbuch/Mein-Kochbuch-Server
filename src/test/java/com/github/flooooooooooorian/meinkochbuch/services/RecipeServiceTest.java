package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.dtos.RecipeCreationDto;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {


    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Captor
    private ArgumentCaptor<Recipe> captor;

    @Test
    void getAllRecipes() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id(1L)
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

        Recipe r1 = Recipe.builder()
                .id(1L)
                .owner(chefUser1)
                .privacy(false)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        Recipe r2 = Recipe.builder()
                .id(2L)
                .owner(chefUser1)
                .privacy(true)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        when(recipeRepository.findAll()).thenReturn(List.of(r1, r2));

        //WHEN

        List<Recipe> result = recipeService.getAllRecipes();

        //THEN
        assertThat(result, Matchers.containsInAnyOrder(r1, r2));
    }

    @Test
    void getRecipeById() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id(1L)
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

        Recipe r1 = Recipe.builder()
                .id(1L)
                .owner(chefUser1)
                .privacy(false)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        when(recipeRepository.findById(1L)).thenReturn(Optional.ofNullable(r1));

        //WHEN

        Recipe result = recipeService.getRecipeById(1L);

        //THEN

        assertThat(result, Matchers.is(r1));
    }

    @Test
    void addRecipe() {
        //GIVEN
        ChefUser chefUser1 = ChefUser.builder()
                .id(1L)
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

        RecipeCreationDto creationDto = RecipeCreationDto.builder()
                .privacy(false)
                .name("test-name")
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .ingredients(List.of())
                .build();

        Recipe r1Result = Recipe.builder()
                .id(1L)
                .owner(chefUser1)
                .name("test-name")
                .privacy(false)
                .portions(4)
                .instruction("test-instructions")
                .duration(40)
                .difficulty(Difficulty.EXPERT)
                .createdAt(Instant.now())
                .ingredients(List.of())
                .build();

        when(recipeRepository.save(any())).thenReturn(r1Result);

        //WHEN

        Recipe result = recipeService.addRecipe(creationDto, chefUser1.getId());

        //THEN

        assertThat(result, Matchers.is(r1Result));
    }
}
