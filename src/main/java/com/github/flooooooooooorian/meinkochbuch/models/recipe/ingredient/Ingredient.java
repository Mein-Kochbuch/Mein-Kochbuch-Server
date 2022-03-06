package com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient;

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
public class Ingredient {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "base_ingredient_id")
    private BaseIngredient baseIngredient;

    private String text;
    private BigDecimal amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ingredient ingredient = (Ingredient) o;

        return Objects.equals(id, ingredient.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
