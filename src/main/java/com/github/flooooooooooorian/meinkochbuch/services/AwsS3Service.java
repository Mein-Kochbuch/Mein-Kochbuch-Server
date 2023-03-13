package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.config.AwsConfig;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsS3Service {

    private final IdUtils idUtils;
    private final S3Client s3;
    private final AwsConfig awsConfig;

    public String uploadImage(InputStream file, String fileName, long size) {
        String fileType = getFileType(fileName);
        String fileKey = idUtils.generateId() + "." + fileType;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .key(fileKey)
                .bucket(awsConfig.getAwsS3BucketName())
                .build();

        RequestBody requestBody = RequestBody.fromInputStream(file, size);

        s3.putObject(objectRequest, requestBody);
        return fileKey;

    }

    public String getPreSignedImageUrl(String key) {
        return s3.utilities().getUrl(GetUrlRequest.builder()
                        .bucket(awsConfig.getAwsS3BucketName())
                        .key(key)
                        .build())
                .toString();
    }

    public String getPreSignedThumbnailUrl(String key) {
        return s3.utilities().getUrl(GetUrlRequest.builder()
                        .bucket(awsConfig.getAwsS3BucketName() + "-resized")
                        .key(key.split("\\.")[0] + "-thumbnail." + key.split("\\.")[1])
                        .build())
                .toString();
    }

    private String getFileType(String fileName) {
        if (fileName == null) {
            return null;
        }
        String[] split = fileName.split("\\.");

        return split[split.length - 1];
    }
}
