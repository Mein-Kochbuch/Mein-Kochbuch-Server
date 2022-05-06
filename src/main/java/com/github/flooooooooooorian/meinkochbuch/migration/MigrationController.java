package com.github.flooooooooooorian.meinkochbuch.migration;

import com.github.flooooooooooorian.meinkochbuch.migration.models.GlobalZutat;
import com.github.flooooooooooorian.meinkochbuch.migration.models.Kochbuchuser;
import com.github.flooooooooooorian.meinkochbuch.migration.models.Zutat;
import com.github.flooooooooooorian.meinkochbuch.migration.services.BaseIngredientMigrationService;
import com.github.flooooooooooorian.meinkochbuch.migration.services.IngredientMigrationService;
import com.github.flooooooooooorian.meinkochbuch.migration.services.UserMigration;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/migration")
@AllArgsConstructor
public class MigrationController {

    private final UserMigration userMigration;
    private final BaseIngredientMigrationService baseIngredientMigrationService;
    private final IngredientMigrationService ingredientMigrationService;

    @PostMapping("users")
    public int migrateUsers(@RequestBody List<Kochbuchuser> kochbuchuserList) {
        return userMigration.migrateUsers(kochbuchuserList);
    }

    @PostMapping("baseingredients")
    public int migrateBaseIngredients(@RequestBody List<GlobalZutat> globalZutats) {
        return baseIngredientMigrationService.migrateBaseIngredients(globalZutats);
    }

    @PostMapping("ingredients")
    public int migrateIngredients(@RequestBody List<Zutat> zutats) {
        return ingredientMigrationService.migrateIngredients(zutats);
    }
}
