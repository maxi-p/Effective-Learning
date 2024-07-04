package com.maxip.filestore.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
@Data
@NoArgsConstructor
@ComponentScan
public class AmazonConfig
{
    @Value("${accessKeyId}")
    public String ACCESS_KEY_ID;
    @Value("${secretKey}")
    public String SECRET_KEY;
    @Value("${bucketName}")
    public String BUCKET_NAME;

    @Bean
    public AmazonS3 s3()
    {
        AWSCredentials credentials = new BasicAWSCredentials(
                Objects.requireNonNull(ACCESS_KEY_ID),
                Objects.requireNonNull(SECRET_KEY));

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}

