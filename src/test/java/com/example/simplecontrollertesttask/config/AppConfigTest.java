package com.example.simplecontrollertesttask.config;

import com.example.simplecontrollertesttask.exception.ConfigException;
import org.apache.commons.codec.digest.HmacUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class AppConfigTest {

    @Test
    void whenAlgorithmIsCorrect_thenHmacUtilsIsCreated() {
        String algorithm="HmacSHA256";
        HmacUtils hmacUtils = new AppConfig("key", algorithm).hmacUtils();
        assertNotNull(hmacUtils);
    }

    @Test
    void whenAlgorithmIsIncorrect_thenThrowConfigException() {
        String algorithm="bad_algorithm";
        Exception e = assertThrows(ConfigException.class, () -> new AppConfig("key", algorithm).hmacUtils());
        assertEquals(
                "Cannot create bean HmacUtils: property security.hashing.algorithm has incorrect value " + algorithm,
                e.getMessage()
        );
    }
}

