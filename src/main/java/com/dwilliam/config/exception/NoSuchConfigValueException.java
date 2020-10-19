package com.dwilliam.config.exception;

public class NoSuchConfigValueException extends ConfigException {

    public NoSuchConfigValueException(String tree) {
        super(tree + " is not set");
    }

}
