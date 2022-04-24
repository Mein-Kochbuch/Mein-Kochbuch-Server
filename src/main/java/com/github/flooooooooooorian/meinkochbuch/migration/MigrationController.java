package com.github.flooooooooooorian.meinkochbuch.migration;

import com.github.flooooooooooorian.meinkochbuch.migration.models.Kochbuchuser;
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

    @PostMapping("users")
    public int migrateUsers(@RequestBody List<Kochbuchuser> kochbuchuserList) {
        return userMigration.migrateUsers(kochbuchuserList);
    }
}
