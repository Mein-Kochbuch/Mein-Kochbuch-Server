package com.github.flooooooooooorian.meinkochbuch;

import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.RatingRepository;
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

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
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
    protected RatingRepository ratingRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @BeforeEach
    public void clear() {
        clearData();
        initTestUser();
        initTestAdmin();
        initTestRecipe();
        initTestRating();
        initTestFavorites();
    }

    @Transactional
    public void clearData() {
        ratingRepository.deleteAll();
        chefUserRepository.deleteAll();
        recipeRepository.deleteAll();
    }

    private void initTestFavorites() {
        ChefUser chefUserById = chefUserRepository.findChefUserById("some-user-id").get();
        Recipe recipe = recipeRepository.findById("test-recipe-id").get();

        chefUserById.setFavoriteRecipes(List.of(recipe));
        chefUserRepository.save(chefUserById);
    }

    private void initTestRating() {
        ratingRepository.save(Rating.builder()
                .value(3)
                .user(ChefUser.ofId("some-user-id"))
                .recipe(Recipe.ofId("test-recipe-id"))
                .build());
    }

    private void initTestRecipe() {
        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-user-id"))
                .instruction("test-recipe-instructions")
                .name("test-recipe-name")
                .build());
        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id-2")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-user-id"))
                .instruction("test-recipe-instructions")
                .name("test-recipe-name")
                .privacy(true)
                .build());
        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id-3")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-admin-id"))
                .instruction("test-recipe-instructions")
                .name("test-recipe-name")
                .build());
        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id-4")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-admin-id"))
                .instruction("test-recipe-instructions")
                .name("test-recipe-name")
                .privacy(true)
                .build());

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
        return jwtUtilsService.createToken(claims, optionalChefUser.get().getId());
    }
}
