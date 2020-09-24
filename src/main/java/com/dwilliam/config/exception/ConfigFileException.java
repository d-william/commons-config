package com.dwilliam.config.exception;

import java.nio.file.Path;

public class ConfigFileException extends ConfigException {

    public ConfigFileException(Path path, String message) {
        super(path.toAbsolutePath().toString() + " : " + message);
    }

}
