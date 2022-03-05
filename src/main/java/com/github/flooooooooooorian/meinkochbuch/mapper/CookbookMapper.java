package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreview;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;

public interface CookbookMapper {

    static CookbookPreview cookbookToCookbookPreview(Cookbook cookbook) {
        return CookbookPreview.builder()
                .id(cookbook.getId())
                .name(cookbook.getName())
                .recipes(cookbook.getContents().stream().map(cookbookContent -> RecipeMapper.recipeToRecipePreviewDto(cookbookContent.getRecipe())).toList())
                .owner(ChefUserMapper.chefUserToChefUserPreviewDto(cookbook.getOwner()))
                .privacy(cookbook.isPrivacy())
                .thumbnail(cookbook.getThumbnail())
                .build();
    }
}
