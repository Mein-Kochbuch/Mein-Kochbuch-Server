package com.github.flooooooooooorian.meinkochbuch.dtos;

import com.github.flooooooooooorian.meinkochbuch.models.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.models.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeCreationDto {

    private String name;
    private String instruction;
    private int duration;
    private Difficulty difficulty;
    private int portions;
    private List<Ingredient> ingredients;
    private boolean privacy;
    private List<Tag> tags;
}
