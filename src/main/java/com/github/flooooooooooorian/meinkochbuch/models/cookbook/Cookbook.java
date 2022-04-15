package com.github.flooooooooooorian.meinkochbuch.models.cookbook;

import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cookbook {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String name;
    private boolean privacy;

    @OneToMany(mappedBy = "cookbook", cascade = CascadeType.ALL)
    private List<CookbookContent> contents;

    @ManyToOne()
    @JoinColumn(name = "owner_id")

    private ChefUser owner;

    @OneToOne()
    private Image thumbnail;

    public double getAverageRating() {
        double ratingSum = this.contents.stream()
                .flatMap(cookbookContent -> {
                    if (cookbookContent.getRecipe().getRatings() == null) {
                        return Stream.empty();
                    } else {
                        return cookbookContent.getRecipe().getRatings().stream()
                                .map(Rating::getValue);
                    }
                })
                .mapToDouble(Double::doubleValue)
                .sum();

        int ratingCount = this.contents.stream()
                .map(cookbookContent -> {
                    if (cookbookContent.getRecipe().getRatings() == null) {
                        return 0;
                    } else {
                        return cookbookContent.getRecipe().getRatings().size();
                    }
                })
                .mapToInt(Integer::intValue)
                .sum();

        if (ratingCount == 0) {
            return 0;
        }
        return ratingSum / ratingCount;
    }

    public static Cookbook ofId(String id) {
        return Cookbook.builder()
                .id(id)
                .build();
    }
}
