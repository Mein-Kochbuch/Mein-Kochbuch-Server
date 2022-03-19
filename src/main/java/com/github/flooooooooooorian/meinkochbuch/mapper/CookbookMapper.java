package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreview;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.CookbookContent;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;

import java.math.BigDecimal;

public interface CookbookMapper {

    static CookbookPreview cookbookToCookbookPreview(Cookbook cookbook) {
        return CookbookPreview.builder()
                .id(cookbook.getId())
                .name(cookbook.getName())
                .owner(ChefUserMapper.chefUserToChefUserPreviewDto(cookbook.getOwner()))
                .thumbnail(ImageMapper.imageToImageDto(cookbook.getThumbnail()))
                .ratingAverage(cookbook.getContents().stream()
                        .map(CookbookContent::getRecipe)
                        .map(Recipe::getRatingAverage)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }

    static CookbookDto cookbookToCookbookDto(Cookbook cookbook) {
        return CookbookDto.builder()
                .id(cookbook.getId())
                .privacy(cookbook.isPrivacy())
                .name(cookbook.getName())
                .owner(ChefUserPreviewDto.builder()
                        .id(cookbook.getOwner().getId())
                        .name(cookbook.getOwner().getName())
                        .build())
                .recipes(cookbook.getContents().stream()
                        .map(CookbookContent::getRecipe)
                        .map(RecipeMapper::recipeToRecipePreviewDto)
                        .toList())
                .ratingAverage(cookbook.getContents().stream()
                        .map(CookbookContent::getRecipe)
                        .map(Recipe::getRatingAverage)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .thumbnail(ImageMapper.imageToImageDto(cookbook.getThumbnail()))
                .build();
    }
}
