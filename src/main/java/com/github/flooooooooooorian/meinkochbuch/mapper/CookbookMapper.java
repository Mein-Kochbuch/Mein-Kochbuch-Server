package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.CookbookPreview;
import com.github.flooooooooooorian.meinkochbuch.models.Cookbook;

public interface CookbookMapper {

    static CookbookPreview cookbookToCookbookPreview(Cookbook cookbook) {
        return CookbookPreview.builder()
                .id(cookbook.getId())
                .name(cookbook.getName())
                .recipes(cookbook.getRecipes().stream().map(RecipeMapper::recipeToRecipePreviewDto).toList())
                .owner(ChefUserMapper.chefUserToChefUserPreviewDto(cookbook.getOwner()))
                .privacy(cookbook.isPrivacy())
                .thumbnail(cookbook.getThumbnail())
                .build();
    }
}
