package com.github.flooooooooooorian.meinkochbuch.dtos.cookbook;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookbookPreviewDto {

    private String id;
    private String name;

    private ChefUserPreviewDto owner;

    private ImageDto thumbnail;

    private double ratingAverage;
}
