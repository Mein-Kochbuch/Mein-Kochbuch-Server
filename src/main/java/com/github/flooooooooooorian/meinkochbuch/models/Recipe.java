package com.github.flooooooooooorian.meinkochbuch.models;

import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Recipe {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private ChefUser owner;

    @NotEmpty
    private String instruction;

    private int duration;
    private Difficulty difficulty;
    private int portions;

    @OneToOne(fetch = FetchType.EAGER)
    private Image thumbnail;

    @OneToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Image> images;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Ingredient> ingredients;

    private boolean privacy;

    private BigInteger relevance;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private List<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Rating> ratings;

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Cookbook> cookbooks;


    public BigDecimal getRatingAverage() {
        if (this.getRatings() == null || this.getRatings().isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal avgRating = BigDecimal.ZERO;
        for (Rating rating : this.getRatings()) {
            avgRating = avgRating.add(rating.getValue());
        }
        return avgRating.divide(BigDecimal.valueOf(this.ratings.size()), RoundingMode.UNNECESSARY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Recipe recipe = (Recipe) o;
        return id != null && Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
