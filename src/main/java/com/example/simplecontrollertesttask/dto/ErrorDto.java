package com.example.simplecontrollertesttask.dto;

import java.util.Map;

public record ErrorDto(String message, Map<String, String> whereToWhy) {
    public ErrorDto(String message) {
        this(message, Map.of());
    }
}
