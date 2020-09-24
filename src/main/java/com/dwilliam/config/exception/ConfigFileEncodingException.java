package com.dwilliam.config.exception;

import java.nio.file.Path;

public class ConfigFileEncodingException extends ConfigFileException {

    public ConfigFileEncodingException(Path path) {
        super(path, "Cannot read the file");
    }

}
