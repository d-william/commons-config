package com.infinity.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.config.exception.NoSuchConfigException;

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

    public static final String DEFAULT_CONFIG = "application.conf";

    static final ObjectMapper MAPPER = new ObjectMapper();
    static final TypeReference<HashMap<String, Object>> TYPE_REFERENCE = new TypeReference<>() {};
    static final Map<String, Config> CONFIGS = new HashMap<>();

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

    public static Boolean getBoolean(String tree) {
        return get(tree);
    }

    public static Boolean getBooleanOrElse(String tree, Boolean elseValue) {
        return getOrElse(tree, elseValue);
    }

    public static Character getCharacter(String tree) {
        String config = defaultConfigFile();
        try {
            return CONFIGS.get(config.substring(0, config.lastIndexOf("."))).getCharacter(tree);
        }
        catch (NullPointerException e) {
            throw new NoSuchConfigException();
        }
    }

    public static Character getCharacterOrElse(String tree, Character elseValue) {
        Character value = getCharacter(tree);
        return value == null ? elseValue : value;
    }

    public static Integer getInteger(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : number.intValue();
    }

    public static Integer getIntegerOrElse(String tree, Integer elseValue) {
        return getNumberOrElse(tree, elseValue).intValue();
    }

    public static Long getLong(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : number.longValue();
    }

    public static Long getLongOrElse(String tree, Long elseValue) {
        return getNumberOrElse(tree, elseValue).longValue();
    }

    public static Float getFloat(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : number.floatValue();
    }

    public static Float getFloatOrElse(String tree, Float elseValue) {
        return getNumberOrElse(tree, elseValue).floatValue();
    }

    public static Double getDouble(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : number.doubleValue();
    }

    public static Double getDoubleOrElse(String tree, Double elseValue) {
        return getNumberOrElse(tree, elseValue).doubleValue();
    }

    public static Byte getByte(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : number.byteValue();
    }

    public static Byte getByteOrElse(String tree, Byte elseValue) {
        return getNumberOrElse(tree, elseValue).byteValue();
    }

    public static Short getShort(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : number.shortValue();
    }

    public static Short getShortOrElse(String tree, Short elseValue) {
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