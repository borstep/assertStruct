package org.assertstruct.converter;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Value;

@Value
public class ValueWrapper implements Wrapper<Object, Object> {
    Object source;
    Object value;

    @JsonValue
    public Object getValue() {
        return value;
    }
}
