package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.tag.RecipeTagging;

import java.util.List;

public interface RecipeMapper {

    static RecipePreviewDto recipeToRecipePreviewDto(Recipe recipe) {
        return RecipePreviewDto.builder()
                .id(recipe.getId())
                .owner(ChefUserMapper.chefUserToChefUserPreviewDto(recipe.getOwner()))
                .name(recipe.getName())
                .ratingAverage(recipe.getRatingAverage())
                .ratingCount(recipe.getRatings().size())
                .thumbnail(ImageMapper.imageToImageDto(recipe.getThumbnail()))
                .build();
    }

    static RecipeDto recipeToRecipeDto(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .owner(ChefUserMapper.chefUserToChefUserPreviewDto(recipe.getOwner()))
                .name(recipe.getName())
                .createdAt(recipe.getCreatedAt())
                .instruction(recipe.getInstruction())
                .duration(recipe.getDuration())
                .difficulty(recipe.getDifficulty())
                .portions(recipe.getPortions())
                .images(recipe.getImages() != null
                        ? recipe.getImages().stream().map(image -> ImageMapper.imageToImageDto(recipe.getThumbnail())).toList()
                        : List.of())
                .ingredients(recipe.getIngredients())
                .privacy(recipe.isPrivacy())
                .ratingAverage(recipe.getRatingAverage())
                .ratingCount(recipe.getRatings() != null ? recipe.getRatings().size() : 0)
                .tags(recipe.getTaggings() != null
                        ? recipe.getTaggings().stream().map(RecipeTagging::getTag).toList()
                        : List.of())
                .ratingAverage(recipe.getRatingAverage())
                .thumbnail(ImageMapper.imageToImageDto(recipe.getThumbnail()))
                .build();
    }
}
