package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeEditDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.mapper.RecipeMapper;
import com.github.flooooooooooorian.meinkochbuch.security.utils.SecurityContextUtil;
import com.github.flooooooooooorian.meinkochbuch.services.RecipeService;
import com.github.flooooooooooorian.meinkochbuch.utils.sorting.RecipeSorting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Slf4j
public class RecipeController {
    private final RecipeService recipeService;
    private final SecurityContextUtil securityContextUtil;

    @GetMapping()
    public List<RecipePreviewDto> getAllRecipes(Principal principal, @RequestParam Optional<RecipeSorting> sort) {
        log.debug("GET All Recipes");
        return recipeService.getAllRecipes(principal != null ? principal.getName() : null, sort).stream()
                .map(RecipeMapper::recipeToRecipePreviewDto)
                .toList();
    }

    @GetMapping("{recipeId}")
    public RecipeDto getRecipeById(@PathVariable String recipeId) {
        log.debug("GET Recipe: " + recipeId);
        return RecipeMapper.recipeToRecipeDto(recipeService.getRecipeById(recipeId, securityContextUtil.getUser()));
    }

    @PostMapping()
    public RecipeDto addRecipe(@Valid @RequestBody RecipeCreationDto editRecipeDto, Principal principal) {
        log.debug("GET Add Recipe");
        return RecipeMapper.recipeToRecipeDto(recipeService.addRecipe(editRecipeDto, principal.getName()));
    }

    @PutMapping("{recipeId}")
    public RecipeDto changeRecipe(@Valid @RequestBody RecipeEditDto editRecipeDto, @PathVariable String recipeId, Principal principal) {
        log.debug("PUT Change Recipe");
        return RecipeMapper.recipeToRecipeDto(recipeService.changeRecipe(recipeId, editRecipeDto, principal.getName()));
    }
}
