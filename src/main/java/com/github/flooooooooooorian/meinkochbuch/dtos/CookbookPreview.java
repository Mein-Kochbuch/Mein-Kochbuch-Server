package com.github.flooooooooooorian.meinkochbuch.dtos;

import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookbookPreview {

    private Long id;
    private String name;
    private boolean privacy;
    private List<RecipePreviewDto> recipes;

    private ChefUserPreviewDto owner;

    private Image thumbnail;
}
