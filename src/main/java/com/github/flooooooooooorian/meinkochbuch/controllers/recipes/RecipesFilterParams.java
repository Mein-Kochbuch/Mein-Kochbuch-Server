package com.github.flooooooooooorian.meinkochbuch.controllers.recipes;

import com.github.flooooooooooorian.meinkochbuch.utils.sorting.RecipeSorting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipesFilterParams {
    @Builder.Default
    public static final int PAGE_SIZE = 50;

    @Builder.Default
    private RecipeSorting sort = RecipeSorting.RELEVANCE;

    @Builder.Default
    private int page = 0;
}
