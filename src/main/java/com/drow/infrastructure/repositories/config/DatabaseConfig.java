package com.drow.infrastructure.repositories.config;

import com.drow.core.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

    private final SecretsManagerClient secretsManagerClient;

    @Bean
    public DataSource dataSource() {
        String secretName = System.getenv(Constants.DATABASE_SECRETS);
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        GetSecretValueResponse getSecretValueResponse;
        try {
            getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            throw e;
        }
        String secret = getSecretValueResponse.secretString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> credentialsMap;

        try {
            credentialsMap = objectMapper.readValue(secret, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            System.err.println("Error converting JSON to Map: " + e.getMessage());
            throw new RuntimeException(e);
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(credentialsMap.get(Constants.DATABASE_HOST));
        dataSource.setUsername(credentialsMap.get(Constants.DATABASE_USERNAME));
        dataSource.setPassword(credentialsMap.get(Constants.DATABASE_PASSWORD));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.example.model");  // Paquete donde se encuentran tus entidades
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(hibernateProperties());

        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.put("hibernate.hbm2ddl.auto", "update");  // O "create" para crear las tablas en la primera ejecución
        properties.put("hibernate.show_sql", "true");

        return properties;
    }
}
