package com.github.flooooooooooorian.meinkochbuch.models.rating;

import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(RatingKey.class)
public class Rating {


    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "recipe_id",
            referencedColumnName = "id")
    private Recipe recipe;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id")
    private ChefUser user;

    private double value;
}
