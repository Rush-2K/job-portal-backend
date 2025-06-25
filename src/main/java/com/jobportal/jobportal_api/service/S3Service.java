package com.jobportal.jobportal_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public S3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public String upload(MultipartFile file, String userId) {
        String fileName = userId + "_" + file.getOriginalFilename();
        String key = "resumes/" + fileName;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            s3Client.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), metadata));

            return s3Client.getUrl(bucketName, fileName).toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

}
