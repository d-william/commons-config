package com.dwilliam.config;

import com.dwilliam.config.exception.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.CharacterCodingException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Config {

    private final Map<String, Object> config = new HashMap<>();

    public Config() {
        this(Configs.defaultConfigFile(), Configs.defaultConfigType(), Configs.defaultConfigLog());
    }

    public Config(String path) {
        this(path, Configs.defaultConfigType(), Configs.defaultConfigLog());
    }

    public Config(File file) {
        this(file, Configs.defaultConfigType(), Configs.defaultConfigLog());
    }

    public Config(Path path) {
        this(path, Configs.defaultConfigType(), Configs.defaultConfigLog());
    }

    public Config(ConfigType type) {
        this(Configs.defaultConfigFile(), type, Configs.defaultConfigLog());
    }

    public Config(boolean log) {
        this(Configs.defaultConfigFile(), Configs.defaultConfigType(), log);
    }

    public Config(String path, ConfigType type) {
        this(path, type, Configs.defaultConfigLog());
    }

    public Config(File file, ConfigType type) {
        this(file, type, Configs.defaultConfigLog());
    }

    public Config(Path path, ConfigType type) {
        this(path, type, Configs.defaultConfigLog());
    }

    public Config(String path, boolean log) {
        this(path, Configs.defaultConfigType(), log);
    }

    public Config(File file, boolean log) {
        this(file, Configs.defaultConfigType(), log);
    }

    public Config(Path path, boolean log) {
        this(path, Configs.defaultConfigType(), log);
    }

    public Config(ConfigType type, boolean log) {
        this(Configs.defaultConfigFile(), type, log);
    }

    public Config(String path, ConfigType type, boolean log) {
        this(new File(path), type, log);
    }

    public Config(File file, ConfigType type, boolean log) {
        this(Path.of(file.getPath()), type, log);
    }

    public Config(Path path, ConfigType type, boolean log) {
        Objects.requireNonNull(path, "path is null");
        Objects.requireNonNull(path, "config type is null");

        String configString;
        try {
            configString = Files.readString(path);
        }
        catch (NoSuchFileException e) {
            throw new NoSuchConfigFileException(path);
        }
        catch (CharacterCodingException e) {
            throw new ConfigFileEncodingException(path);
        }
        catch (IOException e) {
            throw new ConfigFileException(path, e.getMessage());
        }
        catch (OutOfMemoryError e) {
            throw new ConfigFileTooLargeException(path);
        }

        String name = path.toString();
        Configs.CONFIGS.put(name.substring(0, name.lastIndexOf('.')), this);

        switch (type) {
            case JSON:
                HashMap<String, Object> json;
                try { json = Configs.MAPPER.readValue(configString, Configs.TYPE_REFERENCE); }
                catch (JsonParseException e) { throw new ConfigFileJsonParseException(path, "Not a json"); }
                catch (JsonMappingException e) { throw new ConfigFileJsonParseException(path, "Not an json object"); }
                catch (IOException e) { throw new ConfigFileJsonParseException(path, e.getMessage()); }
                initJson(null, json, log);
            break;
            case XML:
                break;
            case YAML:
                break;
        }
    }

    private void initJson(String tree, Map<String, Object> json, boolean log) {
        tree = tree == null ? "" : tree + ".";
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            Object value = entry.getValue();
            String subTree = tree + entry.getKey();
            if (value instanceof Map) {
                initJson(subTree, (Map<String, Object>) value, log);
            }
            else {
                this.config.put(subTree, value);
                if (log) System.out.println("[CONFIG] : " + subTree + " = " + value);
            }
        }
    }

    public int size() {
        return this.config.size();
    }

    public boolean isEmpty() {
        return this.config.isEmpty();
    }

    public boolean containsKey(String tree) {
        return this.config.containsKey(tree);
    }

    public boolean containsValue(Object object) {
        return this.config.containsValue(object);
    }

    public <T> T get(String tree) {
        return (T) this.config.get(tree);
    }

    public <T> T getOrElse(String tree, T elseValue) {
        T value = get(tree);
        return value == null ? elseValue : value;
    }

    public Number getNumber(String tree) {
        return get(tree);
    }

    public Number getNumberOrElse(String tree, Number elseValue) {
        return getOrElse(tree, elseValue);
    }

    public String getString(String tree) {
        return get(tree);
    }

    public String getStringOrElse(String tree, String elseValue) {
        return getOrElse(tree, elseValue);
    }

    public Boolean getBoolean(String tree) {
        return get(tree);
    }

    public Boolean getBooleanOrElse(String tree, Boolean elseValue) {
        return getOrElse(tree, elseValue);
    }

    public Character getCharacter(String tree) {
        String value;
        try { value = get(tree); }
        catch (Exception e) { throw new ClassCastException("Cannot cast " + tree + " value to Character"); }
        if (value.length() != 1) throw new ClassCastException("Cannot cast string of length != 1 to Character");
        return value.charAt(0);
    }

    public Character getCharacterOrElse(String tree, Character elseValue) {
        return getOrElse(tree, elseValue);
    }

    public Integer getInteger(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : number.intValue();
    }

    public Integer getIntegerOrElse(String tree, Integer elseValue) {
        return getNumberOrElse(tree, elseValue).intValue();
    }

    public Long getLong(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : number.longValue();
    }

    public Long getLongOrElse(String tree, Long elseValue) {
        return getNumberOrElse(tree, elseValue).longValue();
    }

    public Float getFloat(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : number.floatValue();
    }

    public Float getFloatOrElse(String tree, Float elseValue) {
        return getNumberOrElse(tree, elseValue).floatValue();
    }

    public Double getDouble(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : number.doubleValue();
    }

    public Double getDoubleOrElse(String tree, Double elseValue) {
        return getNumberOrElse(tree, elseValue).doubleValue();
    }

    public Byte getByte(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : number.byteValue();
    }

    public Byte getByteOrElse(String tree, Byte elseValue) {
        return getNumberOrElse(tree, elseValue).byteValue();
    }

    public Short getShort(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : number.shortValue();
    }

    public Short getShortOrElse(String tree, Short elseValue) {
        return getNumberOrElse(tree, elseValue).shortValue();
    }

    public BigDecimal getBigDecimal(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : BigDecimal.valueOf(number.doubleValue());
    }

    public BigDecimal getBigDecimalOrElse(String tree, BigDecimal elseValue) {
        return BigDecimal.valueOf(getNumberOrElse(tree, elseValue).doubleValue());
    }

    public BigInteger getBigInteger(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : BigInteger.valueOf(number.intValue());
    }

    public BigInteger getBigIntegerOrElse(String tree, BigInteger elseValue) {
        return BigInteger.valueOf(getNumberOrElse(tree, elseValue).intValue());
    }

    public AtomicInteger getAtomicInteger(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : new AtomicInteger(number.intValue());
    }

    public AtomicInteger getAtomicIntegerOrElse(String tree, AtomicInteger elseValue) {
        return new AtomicInteger(getNumberOrElse(tree, elseValue).intValue());
    }

    public AtomicLong getAtomicLong(String tree) {
        Number number = getNumber(tree);
        return number == null ? null : new AtomicLong(number.longValue());
    }

    public AtomicLong getAtomicLongOrElse(String tree, AtomicLong elseValue) {
        return new AtomicLong(getNumberOrElse(tree, elseValue).longValue());
    }

}