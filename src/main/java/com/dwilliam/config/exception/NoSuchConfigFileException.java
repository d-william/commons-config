package com.dwilliam.config.exception;

import java.nio.file.Path;

public class NoSuchConfigFileException extends ConfigFileException {

    public NoSuchConfigFileException(Path path) {
        super(path, "No such file");
    }

}
