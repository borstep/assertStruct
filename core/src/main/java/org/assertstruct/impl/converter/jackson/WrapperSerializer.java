package org.assertstruct.impl.converter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

@AllArgsConstructor
public class WrapperSerializer<T> extends JsonSerializer<T> implements ContextualSerializer, ResolvableSerializer {
    private JsonSerializer<T> _serializer;

    @Override
    public JsonSerializer<T> unwrappingSerializer(NameTransformer unwrapper) {
        return rewrap(_serializer.unwrappingSerializer(unwrapper));
    }

    @Override
    public JsonSerializer<T> replaceDelegatee(JsonSerializer<?> delegatee) {
        return rewrap(_serializer.replaceDelegatee(delegatee));
    }

    @Override
    public JsonSerializer<?> withFilterId(Object filterId) {
        return rewrap(_serializer.withFilterId(filterId));
    }

    @Override
    public JsonSerializer<?> withIgnoredProperties(Set<String> ignoredProperties) {
        return rewrap(_serializer.withIgnoredProperties(ignoredProperties));
    }

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
    public void serializeWithType(T value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        if (gen instanceof SimpleStructGenerator) {
            gen.assignCurrentValue(value);
            if (_serializer instanceof JsonValueSerializer) {
                ((SimpleStructGenerator)gen).skipNextValue();
            }

        }
        _serializer.serializeWithType(value, gen, serializers, typeSer);
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

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType type) throws JsonMappingException {
        _serializer.acceptJsonFormatVisitor(visitor, type);
    }

    @Override
    public boolean isUnwrappingSerializer() {
        return _serializer.isUnwrappingSerializer();
    }

    @Override
    public JsonSerializer<?> getDelegatee() {
        return _serializer.getDelegatee();
    }

    @Override
    public Class<T> handledType() {
        return _serializer.handledType();
    }

    @Override
    public boolean isEmpty(SerializerProvider provider, T value) {
        return _serializer.isEmpty(provider, value);
    }

    @Override
    public Iterator<PropertyWriter> properties() {
        return _serializer.properties();
    }

    @Override
    public boolean usesObjectId() {
        return _serializer.usesObjectId();
    }

    private <E> JsonSerializer<E> rewrap(JsonSerializer<E> newSerializer) {
        if (_serializer==newSerializer) {
            //noinspection unchecked
            return (JsonSerializer<E>) this;
        } else {
            return new WrapperSerializer<>(newSerializer);
        }
    }
}
