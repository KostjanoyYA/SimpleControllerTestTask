package com.example.simplecontrollertesttask.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum HashingAlgorithm {
    HMAC_SHA1("HmacSHA1"),
    HMAC_SHA256("HmacSHA256"),
    HMAC_SHA384("HmacSHA384"),
    HMAC_SHA512("HmacSHA512");

    private final String code;

    private static final List<String> codes = Arrays.stream(values()).map(e -> e.code).toList();

    public static boolean isCorrect(String code) {
        return codes.contains(code);
    }
}
