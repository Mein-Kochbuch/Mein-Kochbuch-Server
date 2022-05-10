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
    public MigrationResponse migrateUsers(@RequestBody List<Kochbuchuser> kochbuchuserList) {
        return MigrationResponse.builder()
                .total(kochbuchuserList.size())
                .successful(userMigration.migrateUsers(kochbuchuserList))
                .build();
    }

    @PostMapping("baseingredients")
    public MigrationResponse migrateBaseIngredients(@RequestBody List<GlobalZutat> globalZutats) {
        return MigrationResponse.builder()
                .total(globalZutats.size())
                .successful(baseIngredientMigrationService.migrateBaseIngredients(globalZutats))
                .build();
    }

    @PostMapping("ingredients")
    public MigrationResponse migrateIngredients(@RequestBody List<Zutat> zutats) {
        return MigrationResponse.builder()
                .total(zutats.size())
                .successful(ingredientMigrationService.migrateIngredients(zutats))
                .build();
    }
}
