package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserProfileDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChefUserProfileMapper {

    private final RecipeMapper recipeMapper;
    private final CookbookMapper cookbookMapper;

    public ChefUserProfileDto chefUserToChefUserProfileDto(ChefUser chefUser) {
        return ChefUserProfileDto.builder()
                .id(chefUser.getId())
                .name(chefUser.getName())
                .recipes(chefUser.getRecipes() != null
                        ? chefUser.getRecipes().stream().map(recipeMapper::recipeToRecipePreviewDto).toList()
                        : List.of())
                .cookbooks(chefUser.getCookbooks() != null
                        ? chefUser.getCookbooks().stream().map(cookbookMapper::cookbookToCookbookPreview).toList()
                        : List.of())
                .build();
    }
}
