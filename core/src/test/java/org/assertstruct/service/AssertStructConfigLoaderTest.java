package org.assertstruct.service;

import org.assertstruct.converter.JsonConverter;
import org.assertstruct.impl.converter.jackson.JacksonConverter;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AssertStructConfigLoaderTest {

    @Test
    void initPropertySetter() {
        Map<String, AssertStructConfigLoader.Setter> setters = AssertStructConfigLoader.initPropertySetter();
        assertTrue(setters.containsKey("indent"));
        assertTrue(setters.containsKey("parserFactoryClasses"));
        assertTrue(setters.containsKey("parserFactoryClass"));
        assertTrue(setters.containsKey("targetPathList"));
    }
    @Test
    void loadProperties() {
        Map<String, String> props = AssertStructConfigLoader.loadProperties();
        assertEquals("uuuu-MM-dd", props.get("config.dateFormatList"));
    }

    @Test
    void loadDefaultConfig() {
        Config config = AssertStructConfigLoader.loadDefaultConfig().internalBuildConfig();
        assertArrayEquals("uuuu-MM-dd".split(","), config.getDateFormats().toArray());
        assertArrayEquals("ISO_INSTANT,uuuu-MM-dd'T'HH:mm:ss,uuuu-MM-dd'T'HH:mm:ss.SSS".split(","), config.getDateTimeFormats().toArray());
    }

    @Test
    void jacksonConfig_OK() {
        Config config = AssertStructConfigLoader.loadDefaultConfig().internalBuildConfig();
        AssertStructService service = new AssertStructService(config);
        JsonConverter jsonConverter = service.getJsonConverter();
        assertInstanceOf(JacksonConverter.class, jsonConverter);
        assertSame(service.with().build().getJsonConverter(), jsonConverter);
    }

}