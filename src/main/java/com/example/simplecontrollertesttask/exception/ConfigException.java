package com.example.simplecontrollertesttask.exception;

public class ConfigException extends RuntimeException{
    public ConfigException(String message, Object... args) {
        super(String.format(message, args));
    }
}
