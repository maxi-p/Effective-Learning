package com.maxip.codeupload.config.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
@PropertySource("classpath:credentials/aws.properties")
public class AmazonConfig
{
    public static final String ACCESS_KEY_ID = "accessKeyId";
    public static final String SECRET_KEY = "secretKey";

    @Autowired
    private Environment environment;

    @Bean
    public AmazonS3 s3()
    {
        AWSCredentials credentials = new BasicAWSCredentials(
                Objects.requireNonNull(environment.getProperty(ACCESS_KEY_ID)),
                Objects.requireNonNull(environment.getProperty(SECRET_KEY)));

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
