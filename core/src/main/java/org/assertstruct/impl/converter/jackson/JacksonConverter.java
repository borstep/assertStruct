package org.assertstruct.impl.converter.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.assertstruct.converter.JsonConverter;
import org.assertstruct.service.Config;
import org.assertstruct.service.exceptions.InitializationFailure;
import org.assertstruct.service.exceptions.MatchingFailure;

import java.io.IOException;
import java.util.ServiceLoader;

@Slf4j
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JacksonConverter implements JsonConverter {
    public static final String PROP_PREFIX="jackson.";
    // custom date format supported removed because of thread safety issue
    public static final String PROP_DATE_FORMAT =PROP_PREFIX+"dateFormat";
    public static final String PROP_MODULES =PROP_PREFIX+"modules";
    ObjectMapper mapper;
    ObjectMapper baseMapper;

    public JacksonConverter(Config config) {
        ObjectMapper baseMapper = buildBaseMapper(config);
        baseMapper=configure(baseMapper);
        this.baseMapper = baseMapper;
        this.mapper = baseMapper.copy();
        mapper.registerModule(new JsonifyWrapperModule());
    }

    private static ObjectMapper buildBaseMapper(Config config) {
        ObjectMapper baseMapper = new ObjectMapper();
        if (config.getExt().containsKey(PROP_MODULES)) {
            String modules = config.getExt().get(PROP_MODULES);
            for (String module : modules.split(",")) {
                try {
                    //noinspection unchecked
                    baseMapper.registerModule(((Class<? extends Module>) Class.forName(module)).newInstance());
                } catch (ClassNotFoundException e) {
                    log.warn("Jackson module {} not found", module);
                } catch (Exception e) {
                    throw new InitializationFailure("Can't instantiate Jackson module " + module, e);
                }
            }
        }
        baseMapper.setDateFormat(new StdDateFormat());
        // TODO add custom date format support
/* support for custom date format removed because of thread safety issue
// ext.jackson.dateFormat=yyyy-MM-dd'T'HH:mm:ss.SSSZ
        if (config.getExt().containsKey(PROP_DATE_FORMAT)) {
            baseMapper.setDateFormat(new SimpleDateFormat(config.getExt().get(PROP_DATE_FORMAT)));
        }
*/
        return baseMapper;
    }

    private static ObjectMapper configure(ObjectMapper mapper) {
        ServiceLoader<JacksonConfigurator> loader = ServiceLoader.load(JacksonConfigurator.class);
        for (JacksonConfigurator configurator : loader) {
            mapper = configurator.configure(mapper);
        }
        return mapper;
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
            throw new MatchingFailure(e);
        }
    }

    @Override
    public <T> T convert(Object value, Class<T> toValueType) {
        return getBaseMapper().convertValue(value, toValueType);
    }

}
