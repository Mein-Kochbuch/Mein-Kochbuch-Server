package com.github.flooooooooooorian.meinkochbuch.security.models;

import org.springframework.security.core.GrantedAuthority;

public enum ChefAuthorities implements GrantedAuthority {

    USER("USER"),
    ADMIN("ADMIN");

    private final String authority;

    ChefAuthorities(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
