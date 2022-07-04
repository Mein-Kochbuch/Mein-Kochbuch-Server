package com.github.flooooooooooorian.meinkochbuch.repository;

import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.BaseIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseIngredientRepository extends JpaRepository<BaseIngredient, String> {
    boolean existsByMigrationId(int id);

    //BaseIngredient findFirstByNameIgnoreCaseOrSingularIgnoreCaseOrSynonymsIsContainingIgnoreCase(String text);
    BaseIngredient findFirstByNameInOrSingularInOrSynonymsContainingIgnoreCase(List<String> texts);
}
