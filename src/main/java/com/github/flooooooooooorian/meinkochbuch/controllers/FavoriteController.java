package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.services.FavorizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavorizeService favorizeService;

    @PostMapping("{recipeId}")
    public boolean favorizeRecipe(Principal principal, @PathVariable String recipeId) {
        return favorizeService.favorizeRecipe(principal.getName(), recipeId);
    }
}
