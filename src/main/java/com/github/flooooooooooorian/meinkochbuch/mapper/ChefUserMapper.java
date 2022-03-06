package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserProfileDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;

import java.util.List;

public interface ChefUserMapper {


    static ChefUserProfileDto chefUserToChefUserProfileDto(ChefUser chefUser) {
        return ChefUserProfileDto.builder()
                .id(chefUser.getId())
                .name(chefUser.getName())
                .recipes(chefUser.getRecipes() != null
                        ? chefUser.getRecipes().stream().map(RecipeMapper::recipeToRecipePreviewDto).toList()
                        : List.of())
                .cookbooks(chefUser.getCookbooks() != null
                        ? chefUser.getCookbooks().stream().map(CookbookMapper::cookbookToCookbookPreview).toList()
                        : List.of())
                .build();
    }

    static ChefUserPreviewDto chefUserToChefUserPreviewDto(ChefUser chefUser) {
        return ChefUserPreviewDto.builder()
                .id(chefUser.getId())
                .name(chefUser.getName())
                .build();
    }
}
