package com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient;


import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BaseIngredient {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String name;
    private String singular;

    @ManyToMany
    private Set<BaseIngredient> children;

    @ElementCollection
    private Set<String> synonyms;

    private Integer migrationId;

    public static BaseIngredient ofId(String id) {
        return BaseIngredient.builder()
                .id(id)
                .build();
    }
}
