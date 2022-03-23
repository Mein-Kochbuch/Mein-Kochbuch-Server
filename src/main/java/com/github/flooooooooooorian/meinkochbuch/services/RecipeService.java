package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeCreationDto;
import com.github.flooooooooooorian.meinkochbuch.exceptions.RecipeDoesNotExistException;
import com.github.flooooooooooorian.meinkochbuch.exceptions.RecipePrivacyForbiddenException;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.repository.IngredientRepository;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import lombok.RequiredArgsConstructor;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private static final Log LOG = LogFactory.getLog(RecipeService.class);
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final IdUtils idUtils;

    public List<Recipe> getAllRecipes(Optional<ChefUser> optionalChefUser) {
        if (optionalChefUser.isPresent()) {
            return recipeRepository.findAllByPrivacyIsFalseOrOwner_Id(optionalChefUser.get().getId());
        }
        return recipeRepository.findAllByPrivacyIsFalseOrOwner_Id(null);
    }

    public Recipe getRecipeById(String recipeId, Optional<ChefUser> optionalChefUser) {

        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeDoesNotExistException("Recipe with id: " + recipeId + " not found!"));
        if ((recipe.isPrivacy() && optionalChefUser.isEmpty()) || (recipe.isPrivacy() && optionalChefUser.isPresent() && !recipe.getOwner().getId().equals(optionalChefUser.get().getId()))) {
            throw new RecipePrivacyForbiddenException("User: " + (optionalChefUser.isPresent() ? optionalChefUser.get().getId() : "Anonymous" + " is not allowed to access Recipe: " + recipeId));
        }
        return recipe;
    }

    @Transactional
    public Recipe addRecipe(RecipeCreationDto recipeCreationDto, String userId) {
        List<Ingredient> ingredients = recipeCreationDto.getIngredients().stream()
                .peek(ingredient -> ingredient.setId(idUtils.generateId()))
                .map(ingredientRepository::save)
                .toList();

        Recipe recipeToAdd = Recipe.builder()
                .id(idUtils.generateId())
                .owner(ChefUser.ofId(userId))
                .name(recipeCreationDto.getName())
                .createdAt(Instant.now())
                .privacy(recipeCreationDto.isPrivacy())
                .difficulty(recipeCreationDto.getDifficulty())
                .duration(recipeCreationDto.getDuration())
                .ingredients(ingredients)
                .instruction(recipeCreationDto.getInstruction())
                .portions(recipeCreationDto.getPortions())
                .build();

        return recipeRepository.save(recipeToAdd);
    }
}
