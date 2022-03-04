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

import javax.transaction.Transactional;
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

    public Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeDoesNotExistException("Recipe with id: " + recipeId + " not found!"));
    }

    @Transactional
    public Recipe addRecipe(RecipeCreationDto recipeCreationDto, Long userId) {
        Recipe recipeToAdd = Recipe.builder()
                .owner(ChefUser.ofId(userId))
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
//        ChefUser updated = userService.addRecipeToUser(user, recipeToAdd);
        //LOG.warn("Updated: " + updated);
//        return recipeToAdd;

//        List<Recipe> recipes = user.getRecipes();
//        recipes.add(recipeToAdd);
//        user.setRecipes(recipes);

        return recipeRepository.save(recipeToAdd);
    }
}
