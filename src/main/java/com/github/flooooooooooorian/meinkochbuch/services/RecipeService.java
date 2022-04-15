package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.dtos.ingredient.IngredientCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.recipe.RecipeEditDto;
import com.github.flooooooooooorian.meinkochbuch.exceptions.RecipeDoesNotExistException;
import com.github.flooooooooooorian.meinkochbuch.exceptions.RecipeEditForbiddenException;
import com.github.flooooooooooorian.meinkochbuch.exceptions.RecipePrivacyForbiddenException;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.repository.IngredientRepository;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import com.github.flooooooooooorian.meinkochbuch.utils.TimeUtils;
import com.github.flooooooooooorian.meinkochbuch.utils.sorting.RecipeSorting;
import lombok.RequiredArgsConstructor;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private static final Log LOG = LogFactory.getLog(RecipeService.class);
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final IdUtils idUtils;
    private final TimeUtils timeUtils;

    public List<Recipe> getAllRecipes(String userId, Optional<RecipeSorting> recipeSorting) {
        return recipeRepository.findAllByPrivacyIsFalseOrOwner_Id(userId, recipeSorting.orElse(RecipeSorting.RELEVANCE).sortValue);
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
                //.map(ingredientService::addIngredient)
                .map(ingredientCreationDto -> Ingredient.builder()
                        .id(idUtils.generateId())
                        .text(ingredientCreationDto.getText())
                        .baseIngredient(ingredientCreationDto.getBaseIngredient())
                        .amount(ingredientCreationDto.getAmount())
                        .build())
                .toList();

        Recipe recipeToAdd = Recipe.builder()
                .id(idUtils.generateId())
                .owner(ChefUser.ofId(userId))
                .name(recipeCreationDto.getName())
                .createdAt(timeUtils.now())
                .privacy(recipeCreationDto.isPrivacy())
                .difficulty(recipeCreationDto.getDifficulty())
                .duration(recipeCreationDto.getDuration())
                .ingredients(ingredients)
                .instruction(recipeCreationDto.getInstruction())
                .portions(recipeCreationDto.getPortions())
                .build();

        recipeToAdd.setRelevance(calculateRelevance(recipeToAdd));

        return recipeRepository.save(recipeToAdd);
    }

    @Transactional
    public Recipe changeRecipe(String recipeId, RecipeEditDto editRecipeDto, String userId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (optionalRecipe.isEmpty()) {
            return addRecipe(RecipeCreationDto.builder()
                            .privacy(editRecipeDto.isPrivacy())
                            .instruction(editRecipeDto.getInstruction())
                            .portions(editRecipeDto.getPortions())
                            .duration(editRecipeDto.getDuration())
                            .difficulty(editRecipeDto.getDifficulty())
                            .name(editRecipeDto.getName())
                            .ingredients(editRecipeDto.getIngredients().stream().map(ingredient -> IngredientCreationDto.builder()
                                            .text(ingredient.getText())
                                            .baseIngredient(ingredient.getBaseIngredient())
                                            .amount(ingredient.getAmount())
                                            .build())
                                    .toList())
                            .images(editRecipeDto.getImages())
                            .tags(editRecipeDto.getTags())
                            .thumbnail(editRecipeDto.getThumbnail())
                            .build(),
                    userId);
        }

        Recipe recipeToUpdate = optionalRecipe.get();

        if (!recipeToUpdate.getOwner().getId().equals(userId)) {
            throw new RecipeEditForbiddenException("Recipe Edit forbidden! Not Owner of Recipe!");
        }

        recipeToUpdate.setRelevance(calculateRelevance(recipeToUpdate));

        recipeToUpdate.getIngredients().stream()
                .map(Ingredient::getId)
                .forEach(ingredientRepository::deleteById);

        List<Ingredient> ingredients = editRecipeDto.getIngredients().stream()
                .map(ingredient -> {
                    ingredient.setId(idUtils.generateId());
                    return ingredient;
                })
                .toList();

        Recipe recipeToAdd = Recipe.builder()
                .id(recipeId)
                .owner(ChefUser.ofId(userId))
                .name(editRecipeDto.getName())
                .createdAt(timeUtils.now())
                .privacy(editRecipeDto.isPrivacy())
                .difficulty(editRecipeDto.getDifficulty())
                .duration(editRecipeDto.getDuration())
                .ingredients(ingredients)
                .instruction(editRecipeDto.getInstruction())
                .portions(editRecipeDto.getPortions())
                .build();

        return recipeRepository.save(recipeToAdd);
    }

    public List<Recipe> getAllRecipesByIds(List<String> ids) {
        return recipeRepository.findAllByIdIn(ids);
    }

    private BigInteger calculateRelevance(Recipe recipe) {
        BigInteger relevance = BigInteger.ZERO;

        if (recipe.getImages() != null) {
            relevance = relevance.add(BigInteger.valueOf(recipe.getImages().size() * 50L));
        }

        if (recipe.getRatings() != null) {
            relevance = relevance.add(BigInteger.valueOf((long) ((recipe.getAverageRating() - 3) * recipe.getRatings().size())));
        }

        return relevance;
    }

    private double calculateAverageRating(Recipe recipe) {
        if (recipe.getRatings() == null || recipe.getRatings().isEmpty()) {
            return 0.0;
        } else {
            double avgRating = 0;
            for (Rating rating : recipe.getRatings()) {
                avgRating = avgRating + rating.getValue();
            }
            return avgRating / recipe.getRatings().size();
        }
    }

    public void recalculateRecipe(String recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeDoesNotExistException("Recipe with id: " + recipeId + " does not exists!"));

        recipe.setAverageRating(calculateAverageRating(recipe));
        recipe.setRelevance(calculateRelevance(recipe));


        recipeRepository.save(recipe);
    }
}
