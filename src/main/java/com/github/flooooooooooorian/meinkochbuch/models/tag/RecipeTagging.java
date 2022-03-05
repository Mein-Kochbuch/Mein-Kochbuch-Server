package com.github.flooooooooooorian.meinkochbuch.models.tag;

import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(TaggingKey.class)
public class RecipeTagging {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "recipe_id",
            referencedColumnName = "id")
    private Recipe recipe;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "tag_id",
            referencedColumnName = "id")
    private Tag tag;
}

