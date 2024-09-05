package org.assertstruct.impl.converter.jackson;

import org.assertstruct.converter.JsonConverter;
import org.assertstruct.converter.JsonConverterFactory;
import org.assertstruct.service.Config;

public class JacksonConverterFactory implements JsonConverterFactory {
    @Override
    public JsonConverter build(Config config) {
        return new JacksonConverter(config);
    }
}
