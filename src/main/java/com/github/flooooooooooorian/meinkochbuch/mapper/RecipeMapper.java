package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.RecipeDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeMapper {

    public RecipePreviewDto convertRecipeToRecipeReviewDto(Recipe recipe) {
        return RecipePreviewDto.builder()
                .id(recipe.getId())
                .owner(ChefUserPreviewDto.builder()
                        .id(recipe.getOwner().getId())
                        .name(recipe.getOwner().getName())
                        .build())
                .name(recipe.getName())
                .ratingAverage(recipe.getRatingAverage())
                .ratingCount(recipe.getRatings().size())
                .thumbnail(recipe.getThumbnail())
                .build();
    }

    public RecipeDto convertRecipeToRecipeDto(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .owner(ChefUserPreviewDto.builder()
                        .id(recipe.getOwner().getId())
                        .name(recipe.getOwner().getName())
                        .build())
                .name(recipe.getName())
                .createdAt(recipe.getCreatedAt())
                .instruction(recipe.getInstruction())
                .duration(recipe.getDuration())
                .difficulty(recipe.getDifficulty())
                .portions(recipe.getPortions())
                .images(recipe.getImages())
                .ingredients(recipe.getIngredients())
                .privacy(recipe.isPrivacy())
                .ratingAverage(recipe.getRatingAverage())
                .ratingCount(recipe.getRatings() != null ? recipe.getRatings().size() : 0)
                .tags(recipe.getTags())
                .ratingAverage(recipe.getRatingAverage())
                .thumbnail(recipe.getThumbnail())
                .build();
    }
}
