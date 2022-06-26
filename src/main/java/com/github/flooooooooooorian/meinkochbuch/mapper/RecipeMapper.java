package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.tag.RecipeTagging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecipeMapper {

    private final ImageMapper imageMapper;
    private final ChefUserMapper chefUserMapper;

    public RecipePreviewDto recipeToRecipePreviewDto(Recipe recipe) {
        return RecipePreviewDto.builder()
                .id(recipe.getId())
                .owner(chefUserMapper.chefUserToChefUserPreviewDto(recipe.getOwner()))
                .name(recipe.getName())
                .ratingAverage(recipe.getAverageRating())
                .ratingCount(recipe.getRatings() != null
                        ? recipe.getRatings().size()
                        : 0)
                .thumbnail(recipe.getThumbnail() != null
                        ? imageMapper.imageToThumbnailDto(recipe.getThumbnail())
                        : null)
                .build();
    }

    public RecipeDto recipeToRecipeDto(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .owner(chefUserMapper.chefUserToChefUserPreviewDto(recipe.getOwner()))
                .name(recipe.getName())
                .instruction(recipe.getInstruction())
                .duration(recipe.getDuration())
                .difficulty(recipe.getDifficulty())
                .portions(recipe.getPortions())
                .images(recipe.getImages() != null
                        ? recipe.getImages().stream().map(imageMapper::imageToImageDto).toList()
                        : List.of())
                .ingredients(recipe.getIngredients() != null
                        ? recipe.getIngredients()
                        : List.of())
                .privacy(recipe.isPrivacy())
                .ratingAverage(recipe.getAverageRating())
                .ratingCount(recipe.getRatings() != null ? recipe.getRatings().size() : 0)
                .tags(recipe.getTaggings() != null
                        ? recipe.getTaggings().stream().map(RecipeTagging::getTag).toList()
                        : List.of())
                .ratingAverage(recipe.getAverageRating())
                .thumbnail(recipe.getThumbnail() != null
                        ? imageMapper.imageToThumbnailDto(recipe.getThumbnail())
                        : null)
                .build();
    }
}
