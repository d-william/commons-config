package config;

import com.infinity.config.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class ConfigTest {

    @Test
    void initDefaultNameConfig() {
        Config.initConfig();
        assertEquals("true", Config.get("shouldBeTrue"));
        assertEquals("ABC", Config.get("shouldBeABC"));
        assertNull(Config.get("nothing"));
    }

    @Test
    void initSystemConfig() {
        Config.initConfig("system.conf");
        assertEquals("false", Config.get("shouldBeFalse"));
        assertEquals("XYZ", Config.get("shouldBeXYZ"));
        assertNull(Config.get("nothing"));
    }

    @Test
    void initJsonConfig() {
        Config.initConfig("json.conf");
        assertEquals("true", Config.get("shouldBeTrue"));
        assertEquals("ABC", Config.get("shouldBeABC"));
        assertNull(Config.get("nothing"));
        assertEquals("OK", Config.get("json.shouldBeOK"));
    }

    @Test
    void initExceptionConfig() {
        Config.initConfig("exception.conf");
        assertNull(Config.get("nullVal"));
        assertNull(Config.get(""));
        assertNull(Config.get("nothing"));
    }

}