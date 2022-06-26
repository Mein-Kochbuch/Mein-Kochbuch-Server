package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.mapper.ImageMapper;
import com.github.flooooooooooorian.meinkochbuch.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @PostMapping
    public ImageDto uploadImage(Principal principal, @RequestParam("file") MultipartFile file) {
        return imageMapper.imageToImageDto(imageService.addImage(principal.getName(), file));
    }

    @DeleteMapping
    void deleteImage(Principal principal, String imageId) {
        imageService.deleteImage(principal.getName(), imageId);
    }
}
