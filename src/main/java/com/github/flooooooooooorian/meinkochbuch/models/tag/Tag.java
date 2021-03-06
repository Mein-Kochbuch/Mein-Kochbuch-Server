package com.github.flooooooooooorian.meinkochbuch.models.tag;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tag {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String name;

    @OneToMany(mappedBy = "tag")
    private List<RecipeTagging> taggins;
}
