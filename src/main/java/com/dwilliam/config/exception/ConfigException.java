package com.dwilliam.config.exception;

public abstract class ConfigException extends RuntimeException {

    public ConfigException(String message) {
        super(message);
    }

}
