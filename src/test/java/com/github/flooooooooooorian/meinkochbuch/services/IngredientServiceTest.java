package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.BaseIngredient;
import com.github.flooooooooooorian.meinkochbuch.repository.BaseIngredientRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ser.Serializers;

import javax.persistence.criteria.CriteriaBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IngredientServiceTest {

    private final BaseIngredientRepository baseIngredientRepository = mock(BaseIngredientRepository.class);
    private final IngredientService ingredientService = new IngredientService(baseIngredientRepository);


    @Test
    void findMatchingBaseIngredient() {
        //GIVEN
        when(baseIngredientRepository.findFirstByNameInOrSingularInOrSynonymsContainingIgnoreCase(any()))
                .thenReturn(BaseIngredient.ofId("1"));

        //WHEN

        BaseIngredient actual = ingredientService.findMatchingBaseIngredient("Test Ingredient String");

        //THEN
        BaseIngredient expected = BaseIngredient.builder()
                .id("1")
                .build();
        assertEquals(actual, Matchers.is(expected));
    }
}
