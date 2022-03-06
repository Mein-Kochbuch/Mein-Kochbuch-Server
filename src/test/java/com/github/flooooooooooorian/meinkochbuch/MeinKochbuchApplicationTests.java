package com.github.flooooooooooorian.meinkochbuch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.stream.Stream;

@SpringBootTest
class MeinKochbuchApplicationTests {

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

    @Test
    void contextLoads() {
    }

}
