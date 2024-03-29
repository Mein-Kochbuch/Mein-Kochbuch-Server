package com.github.flooooooooooorian.meinkochbuch;

import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.CookbookContent;
import com.github.flooooooooooorian.meinkochbuch.models.rating.Rating;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.CookbookRepository;
import com.github.flooooooooooorian.meinkochbuch.repository.RatingRepository;
import com.github.flooooooooooorian.meinkochbuch.repository.RecipeRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import com.github.flooooooooooorian.meinkochbuch.security.service.JwtUtilsService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigInteger;
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
    protected CookbookRepository cookbookRepository;

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
        initCookbooks();
    }

    @Transactional
    public void clearData() {
        cookbookRepository.deleteAll();
        ratingRepository.deleteAll();
        chefUserRepository.deleteAll();
        recipeRepository.deleteAll();
    }

    private void initTestFavorites() {
        ChefUser chefUserById = chefUserRepository.findChefUserById("some-user-id").get();
        Recipe recipe = recipeRepository.findById("test-recipe-id-1").get();

        chefUserById.setFavoriteRecipes(List.of(recipe));
        chefUserRepository.save(chefUserById);
    }

    private void initTestRating() {
        ratingRepository.save(Rating.builder()
                .value(3)
                .user(ChefUser.ofId("some-user-id"))
                .recipe(Recipe.ofId("test-recipe-id-1"))
                .build());
        ratingRepository.save(Rating.builder()
                .value(5)
                .user(ChefUser.ofId("some-user-id"))
                .recipe(Recipe.ofId("test-recipe-id-3"))
                .build());
        ratingRepository.save(Rating.builder()
                .value(4)
                .user(ChefUser.ofId("some-admin-id"))
                .recipe(Recipe.ofId("test-recipe-id-3"))
                .build());
    }

    private void initTestRecipe() {
        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id-1")
                .name("test-recipe-name-A")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-user-id"))
                .instruction("test-recipe-instructions")
                .averageRating(3)
                .relevance(BigInteger.valueOf(-1))
                .build());
        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id-2")
                .name("test-recipe-name-B")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-user-id"))
                .instruction("test-recipe-instructions")
                .privacy(true)
                .build());
        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id-3")
                .name("test-recipe-name-C")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-admin-id"))
                .instruction("test-recipe-instructions")
                .averageRating(4.5)
                .relevance(BigInteger.valueOf(1))
                .build());
        recipeRepository.save(Recipe.builder()
                .id("test-recipe-id-4")
                .name("test-recipe-name-D")
                .createdAt(Instant.now())
                .owner(ChefUser.ofId("some-admin-id"))
                .instruction("test-recipe-instructions")
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

    private void initCookbooks() {
        cookbookRepository.save(Cookbook.builder()
                .id("test-cookbook-id-1")
                .name("test-cookbook-name-1")
                .privacy(false)
                .owner(ChefUser.ofId("some-user-id"))
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.ofId("test-recipe-id-1"))
                        .cookbook(Cookbook.ofId("test-cookbook-id-1"))
                        .build()))
                .build());

        cookbookRepository.save(Cookbook.builder()
                .id("test-cookbook-id-2")
                .name("test-cookbook-name-2")
                .privacy(true)
                .owner(ChefUser.ofId("some-user-id"))
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.ofId("test-recipe-id-1"))
                        .cookbook(Cookbook.ofId("test-cookbook-id-2"))
                        .build()))
                .build());

        cookbookRepository.save(Cookbook.builder()
                .id("test-cookbook-id-3")
                .name("test-cookbook-name-3")
                .privacy(false)
                .owner(ChefUser.ofId("some-admin-id"))
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.ofId("test-recipe-id-1"))
                        .cookbook(Cookbook.ofId("test-cookbook-id-3"))
                        .build()))
                .build());

        cookbookRepository.save(Cookbook.builder()
                .id("test-cookbook-id-4")
                .name("test-cookbook-name-4")
                .privacy(true)
                .owner(ChefUser.ofId("some-admin-id"))
                .contents(List.of(CookbookContent.builder()
                        .recipe(Recipe.ofId("test-recipe-id-1"))
                        .cookbook(Cookbook.ofId("test-cookbook-id-4"))
                        .build()))
                .build());
    }

    protected String getTokenByUserId(String userId) {
        Optional<ChefUser> optionalChefUser = chefUserRepository.findChefUserById(userId);
        optionalChefUser.orElseThrow(() -> new RuntimeException("User not found!"));
        HashMap<String, Object> claims = new HashMap<>();
        return jwtUtilsService.createToken(claims, optionalChefUser.get().getId());
    }
}
