package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.ChefUserProfileDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;

public interface ChefUserMapper {


    static ChefUserProfileDto chefUserToChefUserDto(ChefUser chefUser) {
        return ChefUserProfileDto.builder()
                .id(chefUser.getId())
                .name(chefUser.getName())
                .recipes(chefUser.getRecipes().stream().map(RecipeMapper::recipeToRecipePreviewDto).toList())
                .cookbooks(chefUser.getCookbooks().stream().map(CookbookMapper::cookbookToCookbookPreview).toList())
                .build();
    }

    static ChefUserPreviewDto chefUserToChefUserPreviewDto(ChefUser chefUser) {
        return ChefUserPreviewDto.builder()
                .id(chefUser.getId())
                .name(chefUser.getName())
                .build();
    }
}
