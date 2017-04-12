package io.enlightendev.config;

import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class AWSConfig {

    @Bean
    public AmazonS3Client getS3Client(){
        return new AmazonS3Client();
    }
}
