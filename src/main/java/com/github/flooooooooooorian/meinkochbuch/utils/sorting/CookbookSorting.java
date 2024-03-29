package com.github.flooooooooooorian.meinkochbuch.utils.sorting;

import org.springframework.data.domain.Sort;

public enum CookbookSorting {
    ALPHABETICALLY_DESC(Sort.by("name").descending()),
    ALPHABETICALLY_ASC(Sort.by("name").ascending()),
    RATING(Sort.by("averageRating").descending()),
    RELEVANCE(Sort.by("relevance").descending());

    public final Sort sortValue;

    CookbookSorting(Sort sortValue) {
        this.sortValue = sortValue;
    }
}
