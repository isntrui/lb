package ru.isntrui.lb.services;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;

@Service
public class AwsService {

    private final String accessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
    private final String secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY");
    private final static String REGION = "ru-central1";

    private final S3Client s3Client = S3Client.builder()
            .region(Region.of(REGION))
            .endpointOverride(URI.create("https://storage.yandexcloud.net"))
            .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKeyId, secretAccessKey)
            ))
            .httpClient(ApacheHttpClient.builder()
                    .socketTimeout(Duration.ofMinutes(5))
                    .connectionTimeout(Duration.ofMinutes(2))
                    .build())
            .overrideConfiguration(ClientOverrideConfiguration.builder()
                    .apiCallTimeout(Duration.ofMinutes(10))
                    .apiCallAttemptTimeout(Duration.ofMinutes(5))
                    .build())
            .build();

    public String uploadFile(InputStream inputStream, String fileName, String format) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("lbs3")
                .key(fileName + "." + format)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, inputStream.available()));
        return getFileUrl("lbs3", fileName + "." + format);
    }

    public String getFileUrl(String bucketName, String fileName) {
        return String.format("https://storage.yandexcloud.net/%s/%s", bucketName, fileName);
    }
}