package com.github.flooooooooooorian.meinkochbuch.dtos.cookbook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CookbookCreationDto {

    @NotEmpty
    private String name;
    private boolean privacy;
    private List<String> recipeIds;
}
