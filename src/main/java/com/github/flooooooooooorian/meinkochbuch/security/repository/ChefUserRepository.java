package com.github.flooooooooooorian.meinkochbuch.security.repository;

import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChefUserRepository extends PagingAndSortingRepository<ChefUser, Long> {

    Optional<ChefUser> findChefUserByUsername(String username);

    Optional<ChefUser> findChefUserById(Long id);
}
