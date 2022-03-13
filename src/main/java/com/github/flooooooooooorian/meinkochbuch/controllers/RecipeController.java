package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipePreviewDto;
import com.github.flooooooooooorian.meinkochbuch.mapper.RecipeMapper;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.utils.SecurityContextUtil;
import com.github.flooooooooooorian.meinkochbuch.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private static final Log LOG = LogFactory.getLog(RecipeController.class);
    private final RecipeService recipeService;
    private final SecurityContextUtil securityContextUtil;

    @GetMapping()
    public List<RecipePreviewDto> getAllRecipes() {
        LOG.debug("GET All Recipes");
        return recipeService.getAllRecipes(securityContextUtil.getUser()).stream()
                .map(RecipeMapper::recipeToRecipePreviewDto)
                .toList();
    }

    @GetMapping("{recipeId}")
    public RecipeDto getRecipeById(@PathVariable String recipeId) {
        LOG.debug("GET Recipe: " + recipeId);
        return RecipeMapper.recipeToRecipeDto(recipeService.getRecipeById(recipeId));
    }

    @PostMapping()
    public RecipeDto addRecipe(@Valid @RequestBody RecipeCreationDto recipeCreationDto, Authentication auth) {
        LOG.debug("GET Add Recipe");
        ChefUser user = (ChefUser) auth.getPrincipal();
        return RecipeMapper.recipeToRecipeDto(recipeService.addRecipe(recipeCreationDto, user.getId()));
    }
}
