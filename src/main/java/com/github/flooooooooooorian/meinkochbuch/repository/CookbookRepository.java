package com.github.flooooooooooorian.meinkochbuch.repository;

import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CookbookRepository extends JpaRepository<Cookbook, String> {
    List<Cookbook> findAllByPrivacyIsFalseOrOwner_Id(String id);

    Optional<Cookbook> findById(String id);
}
