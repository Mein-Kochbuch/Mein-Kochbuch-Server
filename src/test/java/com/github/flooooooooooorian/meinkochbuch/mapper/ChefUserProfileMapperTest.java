package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserProfileDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChefUserProfileMapperTest {

    private final RecipeMapper recipeMapperMock = mock(RecipeMapper.class);
    private final CookbookMapper cookbookMapperMock = mock(CookbookMapper.class);
    private final ChefUserProfileMapper chefUserProfileMapper = new ChefUserProfileMapper(recipeMapperMock, cookbookMapperMock);

    @Test
    void chefUserToChefUserProfileDto() {
        //GIVEN
        ChefUser chefUser = MapperUtils.exampleUser;
        when(recipeMapperMock.recipeToRecipePreviewDto(chefUser.getRecipes().get(0)))
                .thenReturn(RecipePreviewDto.builder()
                        .ratingCount(0)
                        .owner(ChefUserPreviewDto.builder()
                                .id("1")
                                .name("test-name")
                                .build())
                        .ratingAverage(0)
                        .id("1")
                        .name("test-recipe")
                        .build());

        when(cookbookMapperMock.cookbookToCookbookPreview(chefUser.getCookbooks().get(0)))
                .thenReturn(CookbookPreviewDto.builder()
                        .id("1")
                        .name("cookbook-name")
                        .thumbnail(ImageDto.builder()
                                .id("1")
                                .build())
                        .owner(ChefUserPreviewDto.builder()
                                .id("1")
                                .name("test-name")
                                .build())
                        .ratingAverage(0)
                        .build());

        //WHEN
        ChefUserProfileDto result = chefUserProfileMapper.chefUserToChefUserProfileDto(chefUser);

        //THEN
        ChefUserProfileDto expected = ChefUserProfileDto.builder()
                .id("1")
                .name("test-name")
                .cookbooks(List.of(CookbookPreviewDto.builder()
                        .id("1")
                        .name("cookbook-name")
                        .thumbnail(ImageDto.builder()
                                .id("1")
                                .build())
                        .owner(ChefUserPreviewDto.builder()
                                .id("1")
                                .name("test-name")
                                .build())
                        .ratingAverage(0)
                        .build()))
                .recipes(List.of(RecipePreviewDto.builder()
                        .ratingCount(0)
                        .owner(ChefUserPreviewDto.builder()
                                .id("1")
                                .name("test-name")
                                .build())
                        .ratingAverage(0)
                        .id("1")
                        .name("test-recipe")
                        .build()))
                .build();

        assertThat(result, Matchers.is(expected));
    }

    @Test
    void chefUserWithNullValuesToChefUserProfileDto() {
        //GIVEN
        ChefUser chefUser = ChefUser.builder()
                .id("1")
                .password("test-password")
                .username("test@email.com")
                .name("test-name")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of(ChefAuthorities.USER))
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        //WHEN
        ChefUserProfileDto result = chefUserProfileMapper.chefUserToChefUserProfileDto(chefUser);

        //THEN
        ChefUserProfileDto expected = ChefUserProfileDto.builder()
                .id("1")
                .name("test-name")
                .cookbooks(List.of())
                .recipes(List.of())
                .build();

        assertThat(result, Matchers.is(expected));
    }
}
