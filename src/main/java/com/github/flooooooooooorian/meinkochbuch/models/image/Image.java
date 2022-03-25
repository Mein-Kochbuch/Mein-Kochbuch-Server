package com.github.flooooooooooorian.meinkochbuch.models.image;

import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Image {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private ChefUser owner;
}
