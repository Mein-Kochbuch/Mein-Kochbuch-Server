package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.services.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImageMapper {

    private final AwsS3Service awsS3Service;

    public ImageDto imageToImageDto(Image image) {
        if (image == null) {
            return null;
        }
        return ImageDto.builder()
                .id(image.getId())
                .url(awsS3Service.getPreSignedImageUrl(image.getKey()))
                .build();
    }

    public ImageDto imageToThumbnailDto(Image image) {
        if (image == null) {
            return null;
        }
        return ImageDto.builder()
                .id(image.getId())
                .url(awsS3Service.getPreSignedThumbnailUrl(image.getKey()))
                .build();
    }
}
