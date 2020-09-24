package com.infinity.config.exception;

import java.nio.file.Path;

public class ConfigFileTooLargeException extends ConfigFileException {

    public ConfigFileTooLargeException(Path path) {
        super(path, "File is too large");
    }

}
