package com.github.flooooooooooorian.meinkochbuch.models;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
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
    @ToString.Exclude
    private Set<BaseIngredient> children;

    @ElementCollection
    private Set<String> synonyms;

    @OneToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Ingredient> ingredients;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BaseIngredient that = (BaseIngredient) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
