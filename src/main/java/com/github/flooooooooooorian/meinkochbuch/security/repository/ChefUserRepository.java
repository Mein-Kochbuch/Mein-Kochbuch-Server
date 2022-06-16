package com.github.flooooooooooorian.meinkochbuch.security.repository;

import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChefUserRepository extends PagingAndSortingRepository<ChefUser, String> {

    Optional<ChefUser> findChefUserByUsername(String username);

    Optional<ChefUser> findChefUserById(String id);

    boolean existsByMigrationId(int id);

    Optional<ChefUser> findByMigrationId(int id);

    @Override
    List<ChefUser> findAll();
}
