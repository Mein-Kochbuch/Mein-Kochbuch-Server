package com.github.flooooooooooorian.meinkochbuch.models;

import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"recipe_id", "user_id"})
})
public class Rating {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private ChefUser user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Rating rating = (Rating) o;
        return id != null && Objects.equals(id, rating.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
