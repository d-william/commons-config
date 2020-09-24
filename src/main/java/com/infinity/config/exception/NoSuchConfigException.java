package com.infinity.config.exception;

import java.nio.file.Path;

public class NoSuchConfigException extends ConfigException {

    public NoSuchConfigException() {
        super("Config does not exist or not init");
    }

}
