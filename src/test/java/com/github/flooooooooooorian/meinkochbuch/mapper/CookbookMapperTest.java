package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreview;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.CookbookContent;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CookbookMapperTest {

    @Test
    void cookbookToCookbookPreview() {
        //GIVEN
        Cookbook cookbook = Cookbook.builder()
                .id("1")
                .owner(ChefUser.builder()
                        .id("1")
                        .name("test-name")
                        .build())
                .name("test-name")
                .privacy(false)
                .thumbnail(Image.builder()
                        .id("1")
                        .owner(ChefUser.ofId("1"))
                        .build())
                .build();

        CookbookContent cookbookContent = CookbookContent.builder()
                .recipe(MapperUtils.exampleRecipe)
                .cookbook(cookbook)
                .build();

        cookbook.setContents(List.of(cookbookContent));

        //WHEN
        CookbookPreview result = CookbookMapper.cookbookToCookbookPreview(cookbook);

        //THEN
        CookbookPreview expected = CookbookPreview.builder()
                .id("1")
                .name("test-name")
                .thumbnail(ImageDto.builder()
                        .id("1")
                        .build())
                .owner(ChefUserPreviewDto.builder()
                        .id("1")
                        .name("test-name")
                        .build())
                .ratingAverage(4)
                .build();

        assertEquals(expected, result);
    }

    @Test
    void cookbookToCookbookDto() {
        //GIVEN
        Cookbook cookbook = Cookbook.builder()
                .id("1")
                .owner(ChefUser.builder()
                        .id("1")
                        .name("test-name")
                        .build())
                .name("test-name")
                .privacy(false)
                .thumbnail(Image.builder()
                        .id("1")
                        .owner(ChefUser.ofId("1"))
                        .build())
                .build();

        CookbookContent cookbookContent = CookbookContent.builder()
                .recipe(MapperUtils.exampleRecipe)
                .cookbook(cookbook)
                .build();

        cookbook.setContents(List.of(cookbookContent));

        //WHEN
        CookbookDto result = CookbookMapper.cookbookToCookbookDto(cookbook);

        //THEN
        CookbookDto expected = CookbookDto.builder()
                .id("1")
                .name("test-name")
                .thumbnail(ImageDto.builder()
                        .id("1")
                        .build())
                .owner(ChefUserPreviewDto.builder()
                        .id("1")
                        .name("test-name")
                        .build())
                .privacy(false)
                .recipes(List.of(RecipePreviewDto.builder()
                        .id("1")
                        .name("test-name")
                        .ratingCount(1)
                        .ratingAverage(4)
                        .owner(ChefUserPreviewDto.builder()
                                .id("1")
                                .name("test-name")
                                .build())
                        .thumbnail(ImageDto.builder()
                                .id("1")
                                .build())
                        .build()))
                .ratingAverage(4)
                .build();

        assertEquals(expected, result);
    }
}