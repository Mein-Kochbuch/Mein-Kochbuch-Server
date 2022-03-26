package com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Ingredient {

    @Id
    private String id;

    @ManyToOne()
    @JoinColumn(name = "base_ingredient_id")
    private BaseIngredient baseIngredient;

    private String text;
    private double amount;
}
