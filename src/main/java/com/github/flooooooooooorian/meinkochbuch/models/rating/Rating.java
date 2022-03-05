package com.github.flooooooooooorian.meinkochbuch.models.rating;

import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"recipe_id", "user_id"})
})
public class Rating {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal value;

    @ManyToOne()
    @JoinColumn(name = "recipe_id")

    private Recipe recipe;

    @ManyToOne()
    @JoinColumn(name = "user_id")

    private ChefUser user;
}
