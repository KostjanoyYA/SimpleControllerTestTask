package com.example.simplecontrollertesttask.config;

import com.example.simplecontrollertesttask.exception.ConfigException;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private final String key;
    private final String algorithm;

    public AppConfig(
            @Value("${security.hashing.key}") String key,
            @Value("${security.hashing.algorithm}") String algorithm
    ) {
        this.key = key;
        this.algorithm = algorithm;
    }

    @Bean
    public HmacUtils hmacUtils() {
        if (!HashingAlgorithm.isCorrect(algorithm))
            throw new ConfigException(
                    "Cannot create bean %s: property %s has incorrect value %s",
                    HmacUtils.class.getSimpleName(),
                    "security.hashing.algorithm",
                    algorithm
            );
        return new HmacUtils(algorithm, key);
    }
}
