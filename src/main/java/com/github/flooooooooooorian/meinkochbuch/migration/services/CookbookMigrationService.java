package com.github.flooooooooooorian.meinkochbuch.migration.services;

import com.github.flooooooooooorian.meinkochbuch.migration.models.Kochbuch;
import com.github.flooooooooooorian.meinkochbuch.migration.models.KochbuchRezept;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.CookbookContent;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.CookbookRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CookbookMigrationService {

    private final CookbookRepository cookbookRepository;
    private final IdUtils idUtils;

    public int migrateCookbooks(List<Kochbuch> kochbuchs) {
        int successfullyCount = 0;
        for (Kochbuch kochbuch : kochbuchs) {
            if (mirgateCookbook(kochbuch)) {
                successfullyCount++;
            }
        }

        return successfullyCount;
    }

    private boolean mirgateCookbook(Kochbuch kochbuch) {

        if (cookbookRepository.existsById(String.valueOf(kochbuch.getId()))) {
            log.warn("MIGRATION Cookbook " + kochbuch.getId() + " already exists!");
            return false;
        }

        Cookbook cookbook = Cookbook.builder()
                .id(String.valueOf(kochbuch.getId()))
                .owner(ChefUser.ofId(String.valueOf(kochbuch.getOwner_id())))
                .name(kochbuch.getName())
                .privacy(kochbuch.isPrivacy())
                .build();

        try {
            cookbookRepository.save(cookbook);
        } catch (IllegalArgumentException ex) {
            log.error("MIGRATION", ex);
            return false;
        }

        return true;
    }

    public int migrateRecipesToCookbooks(List<KochbuchRezept> kochbuchRezepts) {
        int successfullyCount = 0;
        List<String> cookbooksIds = kochbuchRezepts.stream()
                .map(KochbuchRezept::getSammlung_id)
                .map(String::valueOf)
                .distinct()
                .toList();

        for (String cookbookId : cookbooksIds) {
            List<KochbuchRezept> recipesOfCookbook = kochbuchRezepts.stream()
                    .filter(kochbuchRezept -> String.valueOf(kochbuchRezept.getSammlung_id()).equals(cookbookId))
                    .toList();

            if (migrateRecipesToCookbook(cookbookId, recipesOfCookbook)) {
                successfullyCount++;
            }
        }


        return successfullyCount;
    }

    private boolean migrateRecipesToCookbook(String cookbookId, List<KochbuchRezept> recipes) {

        Optional<Cookbook> optionalCookbook = cookbookRepository.findById(cookbookId);

        if (optionalCookbook.isEmpty()) {
            log.error("MIGRATION Cookbook " + cookbookId + " does not exist!");
            return false;
        }

        Cookbook cookbook = optionalCookbook.get();

        cookbook.setContents(recipes.stream()
                .map(KochbuchRezept::getRezept_id)
                .map(String::valueOf)
                .map(Recipe::ofId)
                .map(recipe -> CookbookContent.builder()
                        .cookbook(Cookbook.ofId(cookbookId))
                        .recipe(recipe)
                        .build())
                .collect(Collectors.toList()));

        try {
            cookbookRepository.save(cookbook);
        } catch (IllegalArgumentException ex) {
            log.error("MIGRATION", ex);
            return false;
        }

        return true;
    }
}
