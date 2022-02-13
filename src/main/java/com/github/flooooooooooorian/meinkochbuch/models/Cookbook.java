package com.github.flooooooooooorian.meinkochbuch.models;

import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cookbook {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private boolean privacy;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Recipe> recipes;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private ChefUser owner;

    @OneToOne(fetch = FetchType.EAGER)
    private Image thumbnail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Cookbook cookbook = (Cookbook) o;
        return id != null && Objects.equals(id, cookbook.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
