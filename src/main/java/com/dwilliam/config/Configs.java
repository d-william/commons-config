package com.dwilliam.config;

import com.dwilliam.config.exception.NoSuchConfigValueException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dwilliam.config.exception.NoSuchConfigException;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Configs {

    public static final String DEFAULT_CONFIG;

    static final ObjectMapper MAPPER = new ObjectMapper();
    static final TypeReference<HashMap<String, Object>> TYPE_REFERENCE = new TypeReference<>() {};
    static final Map<String, Config> CONFIGS = new HashMap<>();

    static {
        String property = System.getProperty("config-path");
        if (property != null) DEFAULT_CONFIG = property;
        else {
            String environment = System.getenv("CONFIG_PATH");
            if (environment != null) DEFAULT_CONFIG = environment;
            else DEFAULT_CONFIG = "application.conf";
        }
        init();
    }

    public static Config init() {
        return init(defaultConfigFile());
    }

    public static Config init(String path) {
        return init(path, defaultConfigType());
    }

    public static Config init(File file) {
        return init(file, defaultConfigType());
    }

    public static Config init(Path path) {
        return init(path, defaultConfigType());
    }

    public static Config init(ConfigType type) {
        return init(defaultConfigFile(), type);
    }

    public static Config init(boolean log) {
        return init(defaultConfigFile(), log);
    }

    public static Config init(String path, ConfigType type) {
        return init(path, type, defaultConfigLog());
    }

    public static Config init(File file, ConfigType type) {
        return init(file, type, defaultConfigLog());
    }

    public static Config init(Path path, ConfigType type) {
        return init(path, type, defaultConfigLog());
    }

    public static Config init(String path, boolean log) {
        return init(path, defaultConfigType(), log);
    }

    public static Config init(File file, boolean log) {
        return init(file, defaultConfigType(), log);
    }

    public static Config init(Path path, boolean log) {
        return init(path, defaultConfigType(), log);
    }

    public static Config init(ConfigType type, boolean log) {
        return init(defaultConfigFile(), type, log);
    }

    public static Config init(String path, ConfigType type, boolean log) {
        return init(new File(path), type, log);
    }

    public static Config init(File file, ConfigType type, boolean log) {
        Objects.requireNonNull(file, "file is null");
        return init(Path.of(file.getPath()), type, log);
    }

    public static Config init(Path path, ConfigType type, boolean log) {
        return new Config(path, type, log);
    }

    static String defaultConfigFile() {
        String filename = System.getenv("CONFIG_FILENAME");
        return filename == null ? DEFAULT_CONFIG : filename;
    }

    static ConfigType defaultConfigType() {
        String type = System.getenv("CONFIG_TYPE");
        return type == null ? ConfigType.JSON : ConfigType.valueOf(type);
    }

    static boolean defaultConfigLog() {
        String bool = System.getenv("CONFIG_LOG");
        return bool == null || Boolean.parseBoolean(bool);
    }

    public static Config of(String name) {
        Config config = CONFIGS.get(name);
        if (config == null) throw new NoSuchConfigException();
        return config;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String config, String tree) {
        try {
            return (T) CONFIGS.get(config).get(tree);
        }
        catch (NullPointerException e) {
            throw new NoSuchConfigException();
        }
    }

    public static <T> T get(String tree) {
        String config = defaultConfigFile();
        return (T) get(config.substring(0, config.lastIndexOf(".")), tree);
    }

    public static <T> T getOrElse(String tree, T elseValue) {
        T value = get(tree);
        return value == null ? elseValue : value;
    }

    public static Number getNumber(String tree) {
        return get(tree);
    }

    public static Number getNumberOrElse(String tree, Number elseValue) {
        return getOrElse(tree, elseValue);
    }

    public static String getString(String tree) {
        return get(tree);
    }

    public static String getStringOrElse(String tree, String elseValue) {
        return getOrElse(tree, elseValue);
    }

    public static boolean getBoolean(String tree) {
        return get(tree);
    }

    public static boolean getBooleanOrElse(String tree, boolean elseValue) {
        return getOrElse(tree, elseValue);
    }

    public static char getCharacter(String tree) {
        String config = defaultConfigFile();
        try {
            return CONFIGS.get(config.substring(0, config.lastIndexOf("."))).getCharacter(tree);
        }
        catch (NullPointerException e) {
            throw new NoSuchConfigException();
        }
    }

    public static char getCharacterOrElse(String tree, char elseValue) {
        Character value = getCharacter(tree);
        return value == null ? elseValue : value;
    }

    public static int getInteger(String tree) {
        Number number = getNumber(tree);
        if (number == null) throw new NoSuchConfigValueException(tree);
        return number.intValue();
    }

    public static int getIntegerOrElse(String tree, int elseValue) {
        return getNumberOrElse(tree, elseValue).intValue();
    }

    public static long getLong(String tree) {
        Number number = getNumber(tree);
        if (number == null) throw new NoSuchConfigValueException(tree);
        return number.longValue();
    }

    public static long getLongOrElse(String tree, long elseValue) {
        return getNumberOrElse(tree, elseValue).longValue();
    }

    public static float getFloat(String tree) {
        Number number = getNumber(tree);
        if (number == null) throw new NoSuchConfigValueException(tree);
        return number.floatValue();
    }

    public static float getFloatOrElse(String tree, float elseValue) {
        return getNumberOrElse(tree, elseValue).floatValue();
    }

    public static double getDouble(String tree) {
        Number number = getNumber(tree);
        if (number == null) throw new NoSuchConfigValueException(tree);
        return number.doubleValue();
    }

    public static double getDoubleOrElse(String tree, double elseValue) {
        return getNumberOrElse(tree, elseValue).doubleValue();
    }

    public static byte getByte(String tree) {
        Number number = getNumber(tree);
        if (number == null) throw new NoSuchConfigValueException(tree);
        return number.byteValue();
    }

    public static byte getByteOrElse(String tree, byte elseValue) {
        return getNumberOrElse(tree, elseValue).byteValue();
    }

    public static short getShort(String tree) {
        Number number = getNumber(tree);
        if (number == null) throw new NoSuchConfigValueException(tree);
        return number.shortValue();
    }

    public static short getShortOrElse(String tree, short elseValue) {
        return getNumberOrElse(tree, elseValue).shortValue();
    }

    public static BigDecimal getBigDecimal(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : BigDecimal.valueOf(number.doubleValue());
    }

    public static BigDecimal getBigDecimalOrElse(String tree, BigDecimal elseValue) {
        return BigDecimal.valueOf(getNumberOrElse(tree, elseValue).doubleValue());
    }

    public static BigInteger getBigInteger(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : BigInteger.valueOf(number.intValue());
    }

    public static BigInteger getBigIntegerOrElse(String tree, BigInteger elseValue) {
        return BigInteger.valueOf(getNumberOrElse(tree, elseValue).intValue());
    }

    public static AtomicInteger getAtomicInteger(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : new AtomicInteger(number.intValue());
    }

    public static AtomicInteger getAtomicIntegerOrElse(String tree, AtomicInteger elseValue) {
        return new AtomicInteger(getNumberOrElse(tree, elseValue).intValue());
    }

    public static AtomicLong getAtomicLong(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : new AtomicLong(number.longValue());
    }

    public static AtomicLong getAtomicLongOrElse(String tree, AtomicLong elseValue) {
        return new AtomicLong(getNumberOrElse(tree, elseValue).longValue());
    }

}