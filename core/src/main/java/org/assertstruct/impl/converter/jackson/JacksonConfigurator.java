package org.assertstruct.impl.converter.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface JacksonConfigurator {
    ObjectMapper configure(ObjectMapper mapper);
}
