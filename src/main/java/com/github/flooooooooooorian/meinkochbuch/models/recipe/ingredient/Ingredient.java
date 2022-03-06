package com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

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

}
