package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.InvalidFileException;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.repository.ImageRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AwsS3Service awsS3Service;
    private final ImageRepository imageRepository;
    private final IdUtils idUtils;

    public Image addImage(String userId, MultipartFile file) {
        try {
            String key = awsS3Service.uploadImage(file.getInputStream(), file.getOriginalFilename(), file.getSize());
            Image image = Image.builder()
                    .id(idUtils.generateId())
                    .key(key)
                    .owner(ChefUser.ofId(userId))
                    .build();
            return imageRepository.save(image);
        } catch (IOException e) {
            throw new InvalidFileException("Could not upload image", e);
        }
    }

    public void deleteImage(String userId, String imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow();

        if (!image.getOwner().getId().equals(userId)) {
            throw new RuntimeException("User: " + userId + " is not allowed do delete Image: " + imageId);
        }

        imageRepository.deleteById(imageId);
    }
}
