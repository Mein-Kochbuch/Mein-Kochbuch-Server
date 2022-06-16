package com.github.flooooooooooorian.meinkochbuch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Slf4j
public class AwsConfig {

    @Value("${aws.accessKeyId}")
    private String awsAccessKeyId;

    @Value("${aws.secretAccessKey}")
    private String awsSecretAccessKey;

    @Value("${aws.s3.bucket.name}")
    private String awsS3BucketName;

    public String getAwsS3BucketName() {
        return awsS3BucketName;
    }

    @Bean
    S3Client createS3Client() {
        Region region = Region.EU_CENTRAL_1;
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey)))
                .region(region)
                .build();
    }
}
