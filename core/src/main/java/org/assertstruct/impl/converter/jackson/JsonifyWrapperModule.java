package org.assertstruct.impl.converter.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.type.*;

/**
 * Jackson module which will enforce using WrapperSerializer for all serializations
 */
class JsonifyWrapperModule extends Module {

    private final static Version VERSION = new Version(1, 0, 0, null, null, null);
    private final static String NAME = "JSONIFY_WRAPPER_MODULE";

    @Override
    public String getModuleName() {
        return NAME;
    }

    @Override
    public Version version() {
        return VERSION;
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
}
