package com.github.flooooooooooorian.meinkochbuch.utils.sorting;

public enum RecipeSorting {
    ALPHABETICALLY("name"),
    RATING("averageRating"),
    RELEVANCE("relevance");

    public final String value;

    RecipeSorting(String value) {
        this.value = value;
    }
}
