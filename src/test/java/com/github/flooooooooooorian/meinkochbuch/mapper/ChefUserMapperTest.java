package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserProfileDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreview;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.models.oauth.OAuthDataApple;
import com.github.flooooooooooorian.meinkochbuch.security.models.oauth.OAuthDataGoogle;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

class ChefUserMapperTest {

    @Test
    void chefUserToChefUserProfileDto() {
        //GIVEN
        ChefUser chefUser = MapperUtils.exampleUser;

        //WHEN
        ChefUserProfileDto result = ChefUserMapper.chefUserToChefUserProfileDto(chefUser);

        //THEN
        ChefUserProfileDto expected = ChefUserProfileDto.builder()
                .id(1L)
                .name("test-name")
                .cookbooks(List.of(CookbookPreview.builder()
                        .id(1L)
                        .name("cookbook-name")
                        .privacy(false)
                        .thumbnail(ImageDto.builder()
                                .id(1L)
                                .url("img-url")
                                .build())
                        .owner(ChefUserPreviewDto.builder()
                                .id(1L)
                                .name("test-name")
                                .build())
                        .recipes(List.of(RecipePreviewDto.builder()
                                .owner(ChefUserPreviewDto.builder()
                                        .id(1L)
                                        .name("test-name")
                                        .build())
                                .ratingCount(0)
                                .ratingAverage(BigDecimal.ZERO)
                                .id(1L)
                                .name("test-recipe")
                                .build()))
                        .build()))
                .recipes(List.of(RecipePreviewDto.builder()
                        .ratingCount(0)
                        .owner(ChefUserPreviewDto.builder()
                                .id(1L)
                                .name("test-name")
                                .build())
                        .ratingAverage(BigDecimal.ZERO)
                        .id(1L)
                        .name("test-recipe")
                        .build()))
                .build();

        assertThat(result, Matchers.is(expected));
    }

    @Test
    void chefUserWithNullValuesToChefUserProfileDto() {
        //GIVEN
        ChefUser chefUser = ChefUser.builder()
                .id(1L)
                .password("test-password")
                .username("test@email.com")
                .name("test-name")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of(ChefAuthorities.USER))
                .credentialsNonExpired(true)
                .enabled(true)
                .oAuthDataApple(OAuthDataApple.builder().build())
                .oAuthDataGoogle(OAuthDataGoogle.builder().build())
                .build();

        //WHEN
        ChefUserProfileDto result = ChefUserMapper.chefUserToChefUserProfileDto(chefUser);

        //THEN
        ChefUserProfileDto expected = ChefUserProfileDto.builder()
                .id(1L)
                .name("test-name")
                .cookbooks(List.of())
                .recipes(List.of())
                .build();

        assertThat(result, Matchers.is(expected));
    }

    @Test
    void chefUserToChefUserPreviewDto() {
        //GIVEN
        ChefUser chefUser = MapperUtils.exampleUser;

        //WHEN
        ChefUserPreviewDto result = ChefUserMapper.chefUserToChefUserPreviewDto(chefUser);

        //THEN
        ChefUserPreviewDto expected = ChefUserPreviewDto.builder()
                .id(1L)
                .name("test-name")
                .build();

        assertThat(result, Matchers.is(expected));
    }

    @Test
    void chefUserWithNullValuesToChefUserPreviewDto() {
        //GIVEN
        ChefUser chefUser = ChefUser.builder()
                .id(1L)
                .password("test-password")
                .username("test@email.com")
                .name("test-name")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .authorities(Set.of(ChefAuthorities.USER))
                .credentialsNonExpired(true)
                .enabled(true)
                .oAuthDataApple(OAuthDataApple.builder().build())
                .oAuthDataGoogle(OAuthDataGoogle.builder().build())
                .build();

        //WHEN
        ChefUserPreviewDto result = ChefUserMapper.chefUserToChefUserPreviewDto(chefUser);

        //THEN
        ChefUserPreviewDto expected = ChefUserPreviewDto.builder()
                .id(1L)
                .name("test-name")
                .build();

        assertThat(result, Matchers.is(expected));
    }

}