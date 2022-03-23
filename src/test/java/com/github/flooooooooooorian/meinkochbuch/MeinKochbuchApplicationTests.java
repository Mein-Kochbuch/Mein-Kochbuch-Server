package com.github.flooooooooooorian.meinkochbuch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MeinKochbuchApplicationTests extends  IntegrationTest{

    @Test
    void contextLoads() {
        assertTrue(true);
    }

}
