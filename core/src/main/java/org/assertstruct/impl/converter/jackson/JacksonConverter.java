package org.assertstruct.impl.converter.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.assertstruct.converter.JsonConverter;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JacksonConverter implements JsonConverter {
    ObjectMapper mapper;
    ObjectMapper baseMapper;

    public JacksonConverter() {
        baseMapper = new ObjectMapper();
        baseMapper.registerModule(new JavaTimeModule().enable(JavaTimeFeature.ALWAYS_ALLOW_STRINGIFIED_DATE_TIMESTAMPS));// TODO make configurable
        baseMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        mapper = baseMapper.copy();
        com.fasterxml.jackson.databind.Module module = new com.fasterxml.jackson.databind.Module() {
            @Override
            public String getModuleName() {
                return "JSONIFY";
            }

            @Override
            public Version version() {
                return new Version(1, 0, 0, null, null, null);
            }

            @Override
            public void setupModule(SetupContext context) {
                context.addBeanSerializerModifier(new BeanSerializerModifier() {
/*
                    @Override
                    public JsonSerializer<?> modifyKeySerializer(SerializationConfig config, JavaType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return super.modifyKeySerializer(config, valueType, beanDesc, serializer);
                    }

                    @Override
                    public JsonSerializer<?> modifyEnumSerializer(SerializationConfig config, JavaType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return super.modifyEnumSerializer(config, valueType, beanDesc, serializer);
                    }
*/

                    @Override
                    public JsonSerializer<?> modifyMapLikeSerializer(SerializationConfig config, MapLikeType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return new WrapperSerializer(serializer);
                    }

                    @Override
                    public JsonSerializer<?> modifyMapSerializer(SerializationConfig config, MapType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return new WrapperSerializer(serializer);
                    }

                    @Override
                    public JsonSerializer<?> modifyCollectionLikeSerializer(SerializationConfig config, CollectionLikeType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return new WrapperSerializer(serializer);
                    }

                    @Override
                    public JsonSerializer<?> modifyCollectionSerializer(SerializationConfig config, CollectionType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return new WrapperSerializer(serializer);
                    }

                    @Override
                    public JsonSerializer<?> modifyArraySerializer(SerializationConfig config, ArrayType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return new WrapperSerializer(serializer);
                    }

                    @Override
                    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return new WrapperSerializer(serializer);
                    }

                });
            }
        };
        mapper.registerModule(module);
    }

    public Object pojo2jsonBase(Object value) {
        return baseMapper.convertValue(value, Object.class);
    }

    public Object pojo2json(Object value) {
        try {
            SimpleStructGenerator generator = new SimpleStructGenerator(mapper, false);
            mapper.writeValue(generator, value);
            return generator.getRootObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
