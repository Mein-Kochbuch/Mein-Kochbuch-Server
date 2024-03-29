package com.github.flooooooooooorian.meinkochbuch.repository;

import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, String> {

    List<Recipe> findAllByPrivacyIsFalseOrOwner_Id(String userId, Pageable pageable);

    int countAllByPrivacyIsFalseOrOwner_Id(String userId);

    List<Recipe> findAllByIdIn(List<String> ids);

    boolean existsByMigrationId(int id);

    Optional<Recipe> findByMigrationId(int id);

    @Override
    Optional<Recipe> findById(String id);
}
