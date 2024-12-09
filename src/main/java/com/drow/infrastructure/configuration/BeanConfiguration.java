package com.drow.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import static com.drow.core.Constants.AWS_SECRETS_REGION;

@Configuration
public class BeanConfiguration {

    @Bean
    public SecretsManagerClient provideSecretsManagerClient() {
        String region = System.getenv(AWS_SECRETS_REGION);
        if (region == null || region.isEmpty()) {
            throw new IllegalStateException("La variable de entorno AWS_REGION no está configurada.");
        }
        return SecretsManagerClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.of(region))
                .build();
    }
}
