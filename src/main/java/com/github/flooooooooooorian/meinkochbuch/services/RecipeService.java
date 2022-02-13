package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.dtos.RecipeCreationDto;
import com.github.flooooooooooorian.meinkochbuch.exceptions.RecipeDoesNotExistException;
import com.github.flooooooooooorian.meinkochbuch.models.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.RequiredArgsConstructor;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private static final Log LOG = LogFactory.getLog(RecipeService.class);
    private final RecipeRepository recipeRepository;


    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(String recipeId) {
        return recipeRepository.findById(Long.getLong(recipeId)).orElseThrow(() -> new RecipeDoesNotExistException("Recipe with id: " + recipeId + " not found!"));
    }

    public Recipe addRecipe(RecipeCreationDto recipeCreationDto, ChefUser user) {
        Recipe recipeToAdd = Recipe.builder()
                .owner(user)
                .name(recipeCreationDto.getName())
                .createdAt(Instant.now())
                .privacy(recipeCreationDto.isPrivacy())
                .difficulty(recipeCreationDto.getDifficulty())
                .duration(recipeCreationDto.getDuration())
                .ingredients(recipeCreationDto.getIngredients())
                .instruction(recipeCreationDto.getInstruction())
                .portions(recipeCreationDto.getPortions())
                .tags(recipeCreationDto.getTags())
                .build();

        LOG.error("Add: " + recipeToAdd);
        return recipeRepository.save(recipeToAdd);
    }
}
