package com.github.flooooooooooorian.meinkochbuch;

import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import com.github.flooooooooooorian.meinkochbuch.security.service.JwtUtilsService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class IntegrationTest {
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres")
            .withReuse(true);

    static {
        Stream.of(POSTGRES_CONTAINER).forEach(GenericContainer::start);
        initializePostgres();
    }

    public static void initializePostgres() {
        String username = POSTGRES_CONTAINER.getUsername();
        String password = POSTGRES_CONTAINER.getPassword();
        String url = POSTGRES_CONTAINER.getJdbcUrl();

        System.setProperty("spring.datasource.username", username);
        System.setProperty("spring.datasource.password", password);
        System.setProperty("spring.datasource.url", url);
    }

    @Autowired
    private JwtUtilsService jwtUtilsService;

    @Autowired
    protected ChefUserRepository chefUserRepository;

    @Autowired
    protected RecipeRepository recipeRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @BeforeEach
    public void clear() {
        clearData();
        initTestUser();
        initTestAdmin();
    }

    public void clearData() {
        recipeRepository.deleteAll();
        chefUserRepository.deleteAll();
    }

    private void initTestUser() {
        chefUserRepository.save(ChefUser.builder()
                .id("some-user-id")
                .username("some-user-email@email.de")
                .name("some-user-name")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .authorities(Set.of(ChefAuthorities.USER))
                .password(passwordEncoder.encode("some-user-password"))
                .build());
    }

    private void initTestAdmin() {
        chefUserRepository.save(ChefUser.builder()
                .id("some-admin-id")
                .username("some-admin-email@email.de")
                .name("some-admin-name")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .authorities(Set.of(ChefAuthorities.ADMIN))
                .password(passwordEncoder.encode("some-admin-password"))
                .build());
    }

    protected String getTokenByUserId(String userId) {
        Optional<ChefUser> optionalChefUser = chefUserRepository.findChefUserById(userId);
        optionalChefUser.orElseThrow(() -> new RuntimeException("User not found!"));
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("name", userId);
        return jwtUtilsService.createToken(claims, optionalChefUser.get().getUsername());
    }
}
