package org.assertstruct.impl.converter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class WrapperSerializer<T> extends JsonSerializer<T> implements ContextualSerializer, ResolvableSerializer {
    private JsonSerializer<T> _serializer;
    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (gen instanceof SimpleStructGenerator) {
            gen.assignCurrentValue(value);
            if (_serializer instanceof JsonValueSerializer) {
                ((SimpleStructGenerator)gen).skipNextValue();
            }

        }
        _serializer.serialize(value, gen, serializers);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (_serializer instanceof ContextualSerializer) {
            //noinspection unchecked
            _serializer= (JsonSerializer<T>) ((ContextualSerializer)_serializer).createContextual(prov, property);
        }
        return this;
    }

    @Override
    public void resolve(SerializerProvider provider) throws JsonMappingException {
        if (_serializer instanceof ResolvableSerializer) {
            ((ResolvableSerializer)_serializer).resolve(provider);
        }
    }
}
