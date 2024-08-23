package org.assertstruct.converter;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Value;

@Value
public class ValueWrapper implements Wrapper<Object, Object> {
    Object source;
    @Getter(onMethod_ = @JsonValue)
    Object value;

/*
    @Override
    public String toString() {
        return String.valueOf(value);
    }
*/
}
