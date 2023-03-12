package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.controllers.recipes.RecipesFilterParams;
import com.github.flooooooooooorian.meinkochbuch.controllers.responses.RecipeListResponse;
import com.github.flooooooooooorian.meinkochbuch.controllers.responses.ResponseInfo;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeEditDto;
import com.github.flooooooooooorian.meinkochbuch.mapper.RecipeMapper;
import com.github.flooooooooooorian.meinkochbuch.security.utils.SecurityContextUtil;
import com.github.flooooooooooorian.meinkochbuch.services.RecipeService;
import com.github.flooooooooooorian.meinkochbuch.services.utils.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Slf4j
public class RecipeController {
    private final RecipeService recipeService;
    private final SecurityContextUtil securityContextUtil;
    private final FileService fileService;
    private final RecipeMapper recipeMapper;

    @Value("${domain.url}")
    private String domainUrl;

    @GetMapping()
    public RecipeListResponse getAllRecipes(Principal principal, RecipesFilterParams params) {
        log.debug("GET All Recipes");
        int count = recipeService.recipeCount((principal != null ? Optional.ofNullable(principal.getName()) : Optional.empty()));
        return RecipeListResponse.builder()
                .info(ResponseInfo.builder()
                        .count(count)
                        .pages((count / RecipesFilterParams.PAGE_SIZE))
                        .next(params.getPage() < (count / RecipesFilterParams.PAGE_SIZE) - 1 ? domainUrl + "/api/recipes?page=" + (params.getPage() + 1) : null)
                        .prev(params.getPage() > 0 ? domainUrl + "/api/recipes?page=" + (params.getPage() - 1) : null)
                        .build())
                .results(recipeService.getAllRecipes(principal != null ? principal.getName() : null, params).stream()
                        .map(recipeMapper::recipeToRecipePreviewDto)
                        .toList())
                .build();
    }

    @GetMapping("{recipeId}")
    public RecipeDto getRecipeById(@PathVariable String recipeId) {
        log.debug("GET Recipe: " + recipeId);
        return recipeMapper.recipeToRecipeDto(recipeService.getRecipeById(recipeId, securityContextUtil.getUser()));
    }

    @PostMapping()
    public RecipeDto addRecipe(@Valid @RequestPart RecipeCreationDto editRecipeDto, @RequestPart("file") Optional<MultipartFile> optionalMultipartFile, Principal principal) {
        log.debug("GET Add Recipe");
        if (optionalMultipartFile.isPresent()) {
            MultipartFile file = optionalMultipartFile.get();
            if (!fileService.validateMultiParImage(file)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File Type not supported!");
            }
        }

        return recipeMapper.recipeToRecipeDto(recipeService.addRecipe(editRecipeDto, optionalMultipartFile, principal.getName()));
    }

    @PutMapping("{recipeId}")
    public RecipeDto changeRecipe(@Valid @RequestBody RecipeEditDto editRecipeDto, @PathVariable String recipeId, Principal principal) {
        log.debug("PUT Change Recipe");
        return recipeMapper.recipeToRecipeDto(recipeService.changeRecipe(recipeId, editRecipeDto, principal.getName()));
    }
}
