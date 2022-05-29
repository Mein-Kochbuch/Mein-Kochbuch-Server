package com.github.flooooooooooorian.meinkochbuch.repository;

import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {
}
