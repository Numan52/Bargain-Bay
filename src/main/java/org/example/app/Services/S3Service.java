package org.example.app.Services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class S3Service {
    private final S3Client amazonS3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3Service(
            @Value("${aws.access-key}") String accessKey,
            @Value("${aws.secret-key}") String secretKey,
            @Value("${aws.region}") String region
    ) {
        this.amazonS3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String imageKey = UUID.randomUUID().toString() + "_" + filename;

        if (filename == null) {
            throw new RuntimeException("missing file to upload");
        }

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(String.valueOf(imageKey))
                            .build(),
                    RequestBody.fromInputStream(inputStream, file.getSize())
            );
        }


        return "https://" + bucketName + ".s3.amazonaws.com/" + imageKey;
    }
}
