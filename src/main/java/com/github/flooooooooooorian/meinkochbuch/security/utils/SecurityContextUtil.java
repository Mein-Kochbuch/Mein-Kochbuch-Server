package com.github.flooooooooooorian.meinkochbuch.security.utils;

import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityContextUtil {

    public boolean isLoggedIn() {
        return !(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken);
    }

    public Optional<ChefUser> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isLoggedIn() ? Optional.ofNullable((ChefUser) authentication.getPrincipal()) : Optional.empty();
    }
}
