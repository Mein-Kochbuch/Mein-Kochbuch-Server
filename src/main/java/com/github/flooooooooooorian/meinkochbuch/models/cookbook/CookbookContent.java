package com.github.flooooooooooorian.meinkochbuch.models.cookbook;

import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(CookbookContentKey.class)
public class CookbookContent {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cookbook_id")
    private Cookbook cookbook;
}
