package com.github.flooooooooooorian.meinkochbuch.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefUserProfileDto {

    private Long id;
    private String name;

    private List<RecipePreviewDto> recipes;
    private List<CookbookPreview> cookbooks;
}
