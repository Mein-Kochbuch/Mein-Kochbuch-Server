package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.dtos.rating.RatingDto;
import com.github.flooooooooooorian.meinkochbuch.mapper.RatingMapper;
import com.github.flooooooooooorian.meinkochbuch.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("{recipeId}")
    public RatingDto getOwnRatingFromRecipe(Principal principal, @PathVariable String recipeId) {
        return RatingMapper.ratingToRatingDto(ratingService.getUsersRatingFromRecipe(principal.getName(), recipeId));
    }

}
