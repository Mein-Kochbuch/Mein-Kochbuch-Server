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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CookbookMapperTest {

    private final ImageMapper imageMapperMock = mock(ImageMapper.class);
    private final RecipeMapper recipeMapperMock = mock(RecipeMapper.class);
    private final ChefUserMapper chefUserMapperMock = mock(ChefUserMapper.class);
    private final CookbookMapper cookbookMapper = new CookbookMapper(recipeMapperMock, imageMapperMock, chefUserMapperMock);

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

        CookbookContent cookbookContent1 = CookbookContent.builder()
                .recipe(MapperUtils.exampleRecipe)
                .cookbook(cookbook)
                .build();

        CookbookContent cookbookContent2 = CookbookContent.builder()
                .recipe(MapperUtils.exampleRecipePrivate)
                .cookbook(cookbook)
                .build();

        cookbook.setContents(List.of(cookbookContent1, cookbookContent2));

        when(chefUserMapperMock.chefUserToChefUserPreviewDto(any()))
                .thenReturn(ChefUserPreviewDto.builder()
                        .id("1")
                        .name("test-name")
                        .build());

        when(imageMapperMock.imageToThumbnailDto(any())).thenReturn(ImageDto.builder()
                .id("1")
                .build());

        //WHEN
        CookbookPreview result = cookbookMapper.cookbookToCookbookPreview(cookbook);

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

        CookbookContent cookbookContent1 = CookbookContent.builder()
                .recipe(MapperUtils.exampleRecipe)
                .cookbook(cookbook)
                .build();

        CookbookContent cookbookContent2 = CookbookContent.builder()
                .recipe(MapperUtils.exampleRecipePrivate)
                .cookbook(cookbook)
                .build();

        cookbook.setContents(List.of(cookbookContent1, cookbookContent2));

        when(recipeMapperMock.recipeToRecipePreviewDto(MapperUtils.exampleRecipe)).thenReturn(RecipePreviewDto.builder()
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
                .build());

        when(recipeMapperMock.recipeToRecipePreviewDto(MapperUtils.exampleRecipePrivate)).thenReturn(RecipePreviewDto.builder()
                .id("2")
                .name("test-name")
                .ratingCount(2)
                .ratingAverage(4)
                .owner(ChefUserPreviewDto.builder()
                        .id("1")
                        .name("test-name")
                        .build())
                .thumbnail(ImageDto.builder()
                        .id("1")
                        .build())
                .build());

        when(imageMapperMock.imageToThumbnailDto(cookbook.getThumbnail())).thenReturn(ImageDto.builder()
                .id("1")
                .build());

        //WHEN
        CookbookDto result = cookbookMapper.cookbookToCookbookDto(cookbook);

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
                                .build(),
                        RecipePreviewDto.builder()
                                .id("2")
                                .name("test-name")
                                .ratingCount(2)
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