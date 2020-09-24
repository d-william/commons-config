package com.infinity.config.exception;

import java.nio.file.Path;

public class ConfigFileJsonParseException extends ConfigFileParseException {

    public ConfigFileJsonParseException(Path path, String message) {
        super(path, message);
    }

}
