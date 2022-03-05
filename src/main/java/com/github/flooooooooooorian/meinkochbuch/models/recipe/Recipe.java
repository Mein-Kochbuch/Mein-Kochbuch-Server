package com.github.flooooooooooorian.meinkochbuch.models.recipe;

import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.difficulty.Difficulty;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.models.tag.RecipeTagging;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Recipe {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private Instant createdAt;

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private ChefUser owner;

    @NotEmpty
    private String instruction;

    private int duration;
    private Difficulty difficulty;
    private int portions;

    @OneToOne()
    private Image thumbnail;

    @OneToMany(mappedBy = "owner")
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;

    private boolean privacy;

    private BigInteger relevance;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeTagging> taggings;

    @OneToMany(mappedBy = "recipe")
    private List<Rating> ratings;

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
}
