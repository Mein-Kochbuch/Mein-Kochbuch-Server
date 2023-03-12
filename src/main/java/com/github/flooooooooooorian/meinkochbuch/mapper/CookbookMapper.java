package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.CookbookContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CookbookMapper {

    private final RecipeMapper recipeMapper;
    private final ImageMapper imageMapper;
    private final ChefUserMapper chefUserMapper;

    public CookbookPreviewDto cookbookToCookbookPreview(Cookbook cookbook) {
        return CookbookPreviewDto.builder()
                .id(cookbook.getId())
                .name(cookbook.getName())
                .owner(chefUserMapper.chefUserToChefUserPreviewDto(cookbook.getOwner()))
                .thumbnail(imageMapper.imageToThumbnailDto(cookbook.getThumbnail()))
                .ratingAverage(cookbook.getAverageRating())
                .build();
    }

    public CookbookDto cookbookToCookbookDto(Cookbook cookbook) {
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
                        .map(recipeMapper::recipeToRecipePreviewDto)
                        .toList())
                .ratingAverage(cookbook.getAverageRating())
                .thumbnail(imageMapper.imageToThumbnailDto(cookbook.getThumbnail()))
                .build();
    }
}
