package com.github.flooooooooooorian.meinkochbuch.security.models;

import org.springframework.security.core.GrantedAuthority;

public enum ChefAuthorities implements GrantedAuthority {

    ADMIN("ADMIN");

    private String authority;

    ChefAuthorities(String authority) {
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
