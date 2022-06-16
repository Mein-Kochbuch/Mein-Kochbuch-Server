package com.github.flooooooooooorian.meinkochbuch.services.utils;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileServiceTest {

    private final FileService fileService = new FileService();

    @Test
    void validateMultiParImage_WhenJpg_returnTrue() throws IOException {
        //GIVEN
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource("test_img.jpg").getFile());

        MultipartFile mockMultiPart = new MockMultipartFile("file", "name.jpg", MediaType.IMAGE_JPEG_VALUE, new FileInputStream(file));

        //WHEN

        boolean actual = fileService.validateMultiParImage(mockMultiPart);

        //THEN
        assertTrue(actual);
    }

    @Test
    void validateMultiParImage_whenOther_ReturnFalse() {
        //GIVEN
        MultipartFile mockMultiPart = new MockMultipartFile("name.exe", (byte[]) null);

        //WHEN

        boolean actual = fileService.validateMultiParImage(mockMultiPart);

        //THEN
        assertFalse(actual);
    }
}