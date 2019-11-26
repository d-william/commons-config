package com.infinity.config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Config {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]");

    private static final String WARN              = "[WARNING]";
    private static final String ERROR             = "[ERROR]";
    private static final String INFO              = "[INFO]";
    private static final String CONFIG            = "[CONFIG] : ";
    private static final String CONFIG_IGNORED    = "The config is ignored";
    private static final String CONFIG_KEY_PREFIX = "config.";
    private static final String DEFAULT_CONFIG    = "application.conf";

    private static void warning(String message) {
        System.out.println(dateFormat.format(new Date()) + WARN + CONFIG + message);
    }

    private static void info(String message) {
        System.out.println(dateFormat.format(new Date()) + INFO + CONFIG + message);
    }

    private static void error(String message) {
        System.out.println(dateFormat.format(new Date()) + ERROR + CONFIG + message);
    }

    public static void initConfig() {
        String configName = System.getProperty(CONFIG_KEY_PREFIX + "filename");
        if (configName == null) initConfig(DEFAULT_CONFIG);
        else initConfig(configName);
    }

    public static void initConfig(String configName) {

        if (configName == null) {
            error("Given config name is null");
            throw new IllegalArgumentException("configName is null");
        }

        if ("".equals(configName)) {
            error("Given config name is empty");
            throw new IllegalArgumentException("configName is empty");
        }

        File configFile = new File(configName);
        if (!configFile.exists()) {
            warning(configFile.getAbsolutePath() + " does not exist");
            info(CONFIG_IGNORED);
            return;
        }
        if (!configFile.isFile()) {
            warning(configFile.getAbsolutePath() + " is not a file");
            info(CONFIG_IGNORED);
            return;
        }

        Path configPath;
        String configString;
        try {
            configPath = Paths.get(configFile.getAbsolutePath());
            configString = new String(Files.readAllBytes(configPath));
        }
        catch (Exception e) {
            warning("Cannor read config file : " + configFile.getAbsolutePath());
            info(CONFIG_IGNORED);
            return;
        }

        JSONObject config;
        try { config = new JSONObject(configString); }
        catch (JSONException e) {
            warning("Syntax error in config file : " + configFile.getAbsolutePath());
            info(CONFIG_IGNORED);
            return;
        }

        info("Init " + configName);
        initJson(CONFIG_KEY_PREFIX, config.toMap());
        info("Finish init " + configName);

    }

    @SuppressWarnings("unchecked")
    private static void initJson(String path, Map<String, Object> json) {
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            try {
                Object value = entry.getValue();
                if (value instanceof Map) {
                    initJson(path + entry.getKey() + '.', (Map) value);
                }
                else {
                    if (entry.getKey().isBlank()) {
                        warning("There are a empty key in config");
                        info(entry.getValue().toString() + " value is ignored");
                        continue;
                    }
                    System.setProperty(path + entry.getKey(), entry.getValue().toString());
                    info(entry.getKey() + "=" + entry.getValue().toString());
                }
            }
            catch (NullPointerException e) {
                warning(entry.getKey() + " is null");
                info(entry.getKey() + " key is ignored");
            }
            catch (Exception e) { throw new RuntimeException("error at " + path + entry.getKey(), e); }
        }
    }

    public static String get(String key) {
        if (key.isBlank()) return null;
        try { return System.getProperty(CONFIG_KEY_PREFIX + key); }
        catch (NullPointerException e) { return null; }
    }

    public static String getOrElse(String key, String elseValue) {
        String value = get(key);
        return value == null ? elseValue : value;
    }

}