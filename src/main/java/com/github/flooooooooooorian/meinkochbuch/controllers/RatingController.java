package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.dtos.rating.CreateRatingDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.rating.RatingDto;
import com.github.flooooooooooorian.meinkochbuch.mapper.RatingMapper;
import com.github.flooooooooooorian.meinkochbuch.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final RatingMapper ratingMapper;

    @GetMapping("{recipeId}")
    public RatingDto getOwnRatingFromRecipe(Principal principal, @PathVariable String recipeId) {
        return ratingMapper.ratingToRatingDto(ratingService.getUsersRatingFromRecipe(principal.getName(), recipeId));
    }

    @PutMapping("{recipeId}")
    public RatingDto addRating(Principal principal, @RequestBody @Valid CreateRatingDto ratingDto, @PathVariable String recipeId) {
        return ratingMapper.ratingToRatingDto(ratingService.addRating(principal.getName(), recipeId, ratingDto.getRating()));
    }
}
