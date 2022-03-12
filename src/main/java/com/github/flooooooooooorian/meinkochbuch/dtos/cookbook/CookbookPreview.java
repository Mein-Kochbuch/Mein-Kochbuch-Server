package com.github.flooooooooooorian.meinkochbuch.dtos.cookbook;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookbookPreview {

    private String id;
    private String name;

    private ChefUserPreviewDto owner;

    private ImageDto thumbnail;

    private BigDecimal ratingAverage;
}
