package com.github.flooooooooooorian.meinkochbuch.controllers.responses;

import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeListResponse {
    private ResponseInfo info;
    private List<RecipePreviewDto> results;
}
