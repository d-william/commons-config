package com.dwilliam.config.exception;

public class NoSuchConfigException extends ConfigException {

    public NoSuchConfigException() {
        super("Config does not exist or not init");
    }

}
