package com.infinity.config.exception;

import java.nio.file.Path;

public class NoSuchConfigFileException extends ConfigFileException {

    public NoSuchConfigFileException(Path path) {
        super(path, "No such file");
    }

}
