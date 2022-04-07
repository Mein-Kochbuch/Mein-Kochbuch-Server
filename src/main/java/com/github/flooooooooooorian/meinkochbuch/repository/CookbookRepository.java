package com.github.flooooooooooorian.meinkochbuch.repository;

import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CookbookRepository extends JpaRepository<Cookbook, String> {
    List<Cookbook> findAllByPrivacyIsFalseOrOwner_Id(String id);
}
