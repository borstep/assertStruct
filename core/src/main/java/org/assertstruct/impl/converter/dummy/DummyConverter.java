package org.assertstruct.impl.converter.dummy;

import org.assertstruct.converter.JsonConverter;
import org.assertstruct.converter.ValueWrapper;

import static org.assertstruct.utils.ConversionUtils.*;

/**
 * Dummy implementation of {@link JsonConverter}.
 * Returns the value as is.
 * Will be used if no other converter is not available
 */
public class DummyConverter implements JsonConverter {

    @Override
    public Object pojo2json(Object value) {
        if (isJsonType(value)) {
            return value;
        } else {
            return new ValueWrapper(value, String.valueOf(value));
        }
    }

    @Override
    public <T> T convert(Object value, Class<T> toValueType) {
        if (value==null)
            return null;
        if (toValueType.isInstance(value)) {
            return toValueType.cast(value);
        }
        throw new UnsupportedOperationException("Dummy converted does not support complex conversions");
    }
}
