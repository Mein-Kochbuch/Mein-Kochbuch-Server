package com.github.flooooooooooorian.meinkochbuch.models.cookbook;

import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cookbook {

    @Id
    private String id;

    private String name;
    private boolean privacy;

    @OneToMany(mappedBy = "cookbook")
    private List<CookbookContent> contents;

    @ManyToOne()
    @JoinColumn(name = "owner_id")

    private ChefUser owner;

    @OneToOne()
    private Image thumbnail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cookbook cookbook = (Cookbook) o;

        return Objects.equals(id, cookbook.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
