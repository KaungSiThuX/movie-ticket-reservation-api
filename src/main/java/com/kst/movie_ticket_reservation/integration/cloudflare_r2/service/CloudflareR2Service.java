package com.kst.movie_ticket_reservation.integration.cloudflare_r2.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.kst.movie_ticket_reservation.util.exceptions.CustomS3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudflareR2Service
{
    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

    @Value("${cloudflare.r2.custom-domain}")
    private String customDomain;

    private final S3Client s3Client;

    /**
     * Uploads or updates a file using Spring's MultipartFile.
     * If the key already exists, R2 will overwrite it (updating the file).
     */
    public String uploadObject(String folder, MultipartFile file) throws CustomS3Exception
    {
        String key = folder + "/" + this.generateObjectStoreKey() + "_" + file.getOriginalFilename();

        try
        {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(this.bucketName)
                    .key(key)
                    .contentType(file.getContentType()) // Optional: preserves the file type (e.g., image/jpeg)
                    .build();

            // Stream the file contents directly to R2
            var putResult = s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

            log.info("result is " + putResult);

            return customDomain + "/" + key;
        }
        catch (S3Exception | IOException e)
        {
            log.error("Failed to upload/update file " + key + " in bucket " + bucketName + ": " + e.getMessage());
            throw new CustomS3Exception("Failed to upload/update file " + ": " + e.getMessage());
        }
    }

    private String generateObjectStoreKey()
    {
        return NanoIdUtils.randomNanoId();
    }

    public void deleteObject(String url) throws URISyntaxException, CustomS3Exception
    {
        String key = this.extractKey(url);

        try
        {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(this.bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(request);
        }
        catch (S3Exception e)
        {
            log.error("Failed to delete object " + key + " from bucket " + bucketName + ": " + e.getMessage());
            throw new CustomS3Exception("Failed to delete object " + ": " + e.getMessage());
        }
    }

    private String extractKey(String url) throws URISyntaxException
    {
        URI uri = new URI(url);

        return uri.getPath().substring(1);
    }
}