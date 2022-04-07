package com.github.flooooooooooorian.meinkochbuch.models.cookbook;

import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    public static Cookbook ofId(String id) {
        return Cookbook.builder()
                .id(id)
                .build();
    }
}
