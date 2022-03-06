package com.github.flooooooooooorian.meinkochbuch.models.rating;

import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating = (Rating) o;

        return Objects.equals(id, rating.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
