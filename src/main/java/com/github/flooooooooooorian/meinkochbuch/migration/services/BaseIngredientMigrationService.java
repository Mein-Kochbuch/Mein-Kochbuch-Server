package com.github.flooooooooooorian.meinkochbuch.migration.services;

import com.github.flooooooooooorian.meinkochbuch.migration.models.GlobalZutat;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.ingredient.BaseIngredient;
import com.github.flooooooooooorian.meinkochbuch.repository.BaseIngredientRepository;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BaseIngredientMigrationService {

    private final BaseIngredientRepository baseIngredientRepository;
    private final IdUtils idUtils;

    public int migrateBaseIngredients(List<GlobalZutat> globalZutats) {
        int successfullyCount = 0;
        for (GlobalZutat globalZutat : globalZutats) {
            if (migrateBaseIngredient(globalZutat)) {
                successfullyCount++;
            }
        }

        return successfullyCount;
    }

    public boolean migrateBaseIngredient(GlobalZutat globalZutat) {
        BaseIngredient baseIngredient = BaseIngredient.builder()
                .id(idUtils.generateId())
                .migrationId(globalZutat.getId())
                .name(globalZutat.getName())
                .singular(globalZutat.getSingular())
                .synonyms(globalZutat.getSynonyms() != null ? new HashSet<>(globalZutat.getSynonyms()) : null)
                .children(globalZutat.getChildren() != null ? new HashSet<>(globalZutat.getChildren().stream()
                        .map(GlobalZutat::getId)
                        .map(String::valueOf)
                        .map(BaseIngredient::ofId)
                        .toList()) : null)
                .build();
        if (baseIngredientRepository.existsByMigrationId(globalZutat.getId())) {
            log.warn("MIGRATION BaseIngredient already exists!");
            return false;
        }

        try {
            baseIngredientRepository.save(baseIngredient);
        } catch (IllegalArgumentException ex) {
            log.error("MIGRATION", ex);
            return false;
        }

        return true;
    }
}
