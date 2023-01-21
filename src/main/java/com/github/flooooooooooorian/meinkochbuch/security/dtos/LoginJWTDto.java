package com.github.flooooooooooorian.meinkochbuch.security.dtos;

import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginJWTDto {
    private String jwt;
    private Set<ChefAuthorities> authorities;
    private String username;
}
