package ua.kiev.its.assertstruct.service;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AssertStructConfigLoaderTest {

    @Test
    void initPropertySetter() {
        Map<String, AssertStructConfigLoader.Setter> setters = AssertStructConfigLoader.initPropertySetter();
        System.out.println(setters);
        assertTrue(setters.containsKey("indent"));
        assertTrue(setters.containsKey("parserFactoryClasses"));
        assertTrue(setters.containsKey("parserFactoryClass"));
        assertTrue(setters.containsKey("targetPathList"));
    }
    @Test
    void loadProperties() {
        Map<String, String> props = AssertStructConfigLoader.loadProperties();
        assertEquals("yyyy-MM-dd", props.get("config.dateFormatList"));
    }

    @Test
    void loadDefaultConfig() {
        Config config = AssertStructConfigLoader.loadDefaultConfig().internalBuildConfig();
        assertArrayEquals("yyyy-MM-dd".split(","), config.getDateFormats().toArray());
        assertArrayEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z',yyyy-MM-dd'T'HH:mm:ss,yyyy-MM-dd'T'HH:mm:ss.SSS".split(","), config.getDateTimeFormats().toArray());
    }

}