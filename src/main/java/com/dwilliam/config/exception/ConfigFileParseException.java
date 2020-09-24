package com.dwilliam.config.exception;

import java.nio.file.Path;

public abstract class ConfigFileParseException extends ConfigFileException {

    public ConfigFileParseException(Path path, String message) {
        super(path, message);
    }

}
