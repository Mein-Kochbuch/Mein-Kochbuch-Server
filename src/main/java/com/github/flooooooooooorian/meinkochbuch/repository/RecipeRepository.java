package com.github.flooooooooooorian.meinkochbuch.repository;

import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, String> {

    List<Recipe> findAll();

    List<Recipe> findAllByPrivacyIsFalseOrOwner_Id(String userId);

    List<Recipe> findAllByIdIn(List<String> ids);

    @Override
    Optional<Recipe> findById(String id);
}
