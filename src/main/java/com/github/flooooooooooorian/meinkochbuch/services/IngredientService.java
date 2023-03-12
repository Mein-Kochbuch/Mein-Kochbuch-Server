package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.BaseIngredient;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.Ingredient;
import com.github.flooooooooooorian.meinkochbuch.repository.BaseIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final BaseIngredientRepository baseIngredientRepository;

    public BaseIngredient findMatchingBaseIngredient(String ingredientText) {
        String[] allIngredientWords = ingredientText.trim().split(" ");

        List<String> ingredientWords = Arrays.stream(allIngredientWords)
                .filter(word -> word.matches(".*//d.*"))
                .toList();

        //for (String word:ingredientWords) {
        //    BaseIngredient baseIngredient =  baseIngredientRepository.findFirstByNameInOrSingularInOrSynonymsContainingIgnoreCase(word);
        //    if (baseIngredient != null) {
        //        return baseIngredient;
        //    }
        //}
        //return null;
        return baseIngredientRepository.findFirstByNameInOrSingularInOrSynonymsContainingIgnoreCase(ingredientWords);
    }


}
