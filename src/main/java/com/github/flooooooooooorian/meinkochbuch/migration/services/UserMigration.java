package com.github.flooooooooooorian.meinkochbuch.migration.services;

import com.github.flooooooooooorian.meinkochbuch.migration.models.Kochbuchuser;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.models.oauth.OAuthApple;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class UserMigration {

    private final IdUtils idUtils;
    private final ChefUserRepository chefUserRepository;

    public int migrateUsers(List<Kochbuchuser> kochbuchuserList) {
        int successfullyCount = 0;
        for (Kochbuchuser kochbuchuser : kochbuchuserList) {
            if (migrateUser(kochbuchuser)) {
                successfullyCount++;
            }
        }

        return successfullyCount;
    }

    public boolean migrateUser(Kochbuchuser kochbuchuser) {
        ChefUser migratedUser = ChefUser.builder()
                .id(idUtils.generateId())
                .migrationId(kochbuchuser.getId())
                .name(kochbuchuser.getUsername())
                .enabled(kochbuchuser.isActive())
                .credentialsNonExpired(false)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .password(kochbuchuser.getPassword())
                .joinedAt(kochbuchuser.getDateJoined())
                .username(kochbuchuser.getEmail())
                .lastLogin(kochbuchuser.getLastLogin())
                .build();

        if (kochbuchuser.getAppleId() != null) {
            migratedUser.setOAuthApple(OAuthApple.builder()
                    .id(idUtils.generateId())
                    .accessToken(kochbuchuser.getAppleId())
                    .build());
        }

        if (kochbuchuser.isSuperUser()) {
            migratedUser.setAuthorities(Set.of(ChefAuthorities.ADMIN));
        }

        if (chefUserRepository.existsByMigrationId(migratedUser.getMigrationId())) {
            log.warn("MIGRATION User already exists!");
            return false;
        }

        try {
            chefUserRepository.save(migratedUser);
        } catch (IllegalArgumentException ex) {
            log.error("MIGRATION", ex);
            return false;
        }

        return true;
    }
}
