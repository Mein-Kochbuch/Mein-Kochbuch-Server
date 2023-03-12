package com.github.flooooooooooorian.meinkochbuch.services.utils;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final Tika tika = new Tika();
    private final List<String> IMAGE_FILE_TYPES = List.of("image/png", "image/jpeg", "image/jpg");

    public boolean validateMultiParImage(MultipartFile multipartFile) {
        try {
            String type = tika.detect(multipartFile.getInputStream());
            return IMAGE_FILE_TYPES.contains(type);
        } catch (IOException e) {
            return false;
        }
    }
}
