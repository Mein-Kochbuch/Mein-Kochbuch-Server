package com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient;


import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BaseIngredient {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String singular;

    @ManyToMany
    private Set<BaseIngredient> children;

    @ElementCollection
    private Set<String> synonyms;

    @OneToMany(mappedBy = "baseIngredient")
    private List<Ingredient> ingredients;
}
