package com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty;

public enum Difficulty {
    VERY_EASY,
    EASY,
    MEDIUM,
    HARD,
    EXPERT;

    public static Difficulty ofId(int id) {
        return switch (id) {
            case 1 -> VERY_EASY;
            case 2 -> EASY;
            case 4 -> HARD;
            case 5 -> EXPERT;
            default -> MEDIUM;
        };
    }
}
