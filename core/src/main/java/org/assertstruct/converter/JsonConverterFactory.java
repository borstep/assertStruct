package org.assertstruct.converter;

import org.assertstruct.service.Config;

public interface JsonConverterFactory {
    JsonConverter build(Config config);
}
