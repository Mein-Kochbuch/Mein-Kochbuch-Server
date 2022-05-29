package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.repository.ImageRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AwsS3Service awsS3Service;
    private final ImageRepository imageRepository;
    private final IdUtils idUtils;

    public Image addImage(String userId, MultipartFile file) {
        String imageUrl = awsS3Service.uploadImage(file);
        Image image = Image.builder()
                .id(idUtils.generateId())
                .url(imageUrl)
                .owner(ChefUser.ofId(userId))
                .build();
        return imageRepository.save(image);
    }

    public void deleteImage(String userId, String imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow();

        if (!image.getOwner().getId().equals(userId)) {
            throw new RuntimeException("User: " + userId + " is not allowed do delete Image: " + imageId);
        }

        imageRepository.deleteById(imageId);
    }
}
