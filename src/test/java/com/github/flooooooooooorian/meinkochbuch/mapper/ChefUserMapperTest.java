package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

class ChefUserMapperTest {

    private final ChefUserMapper chefUserMapper = new ChefUserMapper();

    @Test
    void chefUserToChefUserPreviewDto() {
        //GIVEN
        ChefUser chefUser = MapperUtils.exampleUser;

        //WHEN
        ChefUserPreviewDto result = chefUserMapper.chefUserToChefUserPreviewDto(chefUser);

        //THEN
        ChefUserPreviewDto expected = ChefUserPreviewDto.builder()
                .id("1")
                .name("test-name")
                .build();

        assertThat(result, Matchers.is(expected));
    }

    @Test
    void chefUserWithNullValuesToChefUserPreviewDto() {
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
        ChefUserPreviewDto result = chefUserMapper.chefUserToChefUserPreviewDto(chefUser);

        //THEN
        ChefUserPreviewDto expected = ChefUserPreviewDto.builder()
                .id("1")
                .name("test-name")
                .build();

        assertThat(result, Matchers.is(expected));
    }

}