package com.github.flooooooooooorian.meinkochbuch.security.models.oauth;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthDataGoogle {
    @Id
    private String id;
}
