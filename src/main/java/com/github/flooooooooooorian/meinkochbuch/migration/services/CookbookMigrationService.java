package com.github.flooooooooooorian.meinkochbuch.migration.services;

import com.github.flooooooooooorian.meinkochbuch.migration.models.Kochbuch;
import com.github.flooooooooooorian.meinkochbuch.migration.models.KochbuchRezept;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.CookbookContent;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.CookbookRepository;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
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
    private final ChefUserRepository chefUserRepository;
    private final RecipeRepository recipeRepository;
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

        if (cookbookRepository.existsByMigrationId(kochbuch.getId())) {
            log.warn("MIGRATION Cookbook " + kochbuch.getId() + " already exists!");
            return false;
        }

        Optional<ChefUser> optionalChefUser = chefUserRepository.findByMigrationId(kochbuch.getOwner_id());

        if (optionalChefUser.isEmpty()) {
            log.error("MIGRATION Cookbook " + kochbuch.getOwner_id() + " User not found!");
            return false;
        }

        Cookbook cookbook = Cookbook.builder()
                .id(idUtils.generateId())
                .migrationId(kochbuch.getId())
                .owner(optionalChefUser.get())
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
        List<Integer> cookbooksIds = kochbuchRezepts.stream()
                .map(KochbuchRezept::getSammlung_id)
                .distinct()
                .toList();

        for (int cookbookId : cookbooksIds) {
            List<KochbuchRezept> recipesOfCookbook = kochbuchRezepts.stream()
                    .filter(kochbuchRezept -> kochbuchRezept.getSammlung_id() == cookbookId)
                    .toList();

            if (migrateRecipesToCookbook(cookbookId, recipesOfCookbook)) {
                successfullyCount++;
            }
        }


        return successfullyCount;
    }

    private boolean migrateRecipesToCookbook(int cookbookId, List<KochbuchRezept> recipes) {

        Optional<Cookbook> optionalCookbook = cookbookRepository.findByMigrationId(cookbookId);

        if (optionalCookbook.isEmpty()) {
            log.error("MIGRATION Cookbook " + cookbookId + " does not exist!");
            return false;
        }

        Cookbook cookbook = optionalCookbook.get();

        cookbook.setContents(recipes.stream()
                .map(KochbuchRezept::getRezept_id)
                .map(recipeRepository::findByMigrationId)
                .map(recipe -> CookbookContent.builder()
                        .cookbook(cookbook)
                        .recipe(recipe.get())
                        .build())
                .collect(Collectors.toList()));

        Optional<Image> optionalThumbnail = cookbook.getContents().stream()
                .map(CookbookContent::getRecipe)
                .filter(recipe -> recipe.getThumbnail() != null)
                .findFirst()
                .map(Recipe::getThumbnail);

        optionalThumbnail.ifPresent(cookbook::setThumbnail);

        try {
            cookbookRepository.save(cookbook);
        } catch (IllegalArgumentException ex) {
            log.error("MIGRATION", ex);
            return false;
        }

        return true;
    }
}
