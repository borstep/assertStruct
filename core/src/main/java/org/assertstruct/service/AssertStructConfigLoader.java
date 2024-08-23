package org.assertstruct.service;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class AssertStructConfigLoader {
    public static final String PRIORITY = "priority";
    public static final String EXT_PREFIX = "ext.";
    public static final String CONFIG_PREFIX = "config.";
    private static final Map<Class<?>, Function<String, Object>> convertors = initConvertors();
    private static final Map<String, Setter> setters = initPropertySetter();

    public static AssertStructService loadDefaultService() {
        return loadDefaultConfig().build();
    }

    static Config.ConfigBuilder loadDefaultConfig() {
        Config.ConfigBuilder configBuilder = new Config.ConfigBuilder();
        Map<String, String> properties = loadProperties();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(CONFIG_PREFIX)) {
                String name = key.substring(CONFIG_PREFIX.length());
                if (setters.containsKey(name)) {
                    setters.get(name).apply(configBuilder, entry.getValue());
                } else {
                    log.warn("Unknown config property: " + name);
                }
            } else if (key.startsWith(EXT_PREFIX)) {
                String name = key.substring(EXT_PREFIX.length());
                configBuilder.ext(name, entry.getValue());
            }
        }
        ServiceLoader<AssertStructConfigurator> loader = ServiceLoader.load(AssertStructConfigurator.class);
        for (AssertStructConfigurator configurator : loader) {
            configBuilder = configurator.configure(configBuilder);
        }
        return configBuilder;
    }

    @SuppressWarnings("unchecked")
    static Map<String, String> loadProperties() {
        try {
            Enumeration<URL> resources = AssertStructConfigLoader.class.getClassLoader().getResources("assert-struct.properties");
            Map<?, ?> properties = Collections.list(resources).stream()
                    .map(AssertStructConfigLoader::loadPropertiesResource)
                    .sorted(Comparator.comparingInt(AssertStructConfigLoader::getPriority))
                    .flatMap(p -> p.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            properties.remove(PRIORITY);
            return (Map<String, String>) properties;
        } catch (Exception e) {
            throw new InitializationFailure("Can't read assert-struct.properties", e);
        }
    }

    private static Properties loadPropertiesResource(URL url) {
        try {
            Properties properties = new Properties();
            properties.load(url.openStream());
            properties.put(PRIORITY, Integer.valueOf(properties.getProperty(PRIORITY, "0")));
            return properties;
        } catch (IOException e) {
            throw new InitializationFailure("Can't read assert-struct.properties from " + url, e);
        }
    }

    private static int getPriority(Properties p) {
        return (Integer) p.getOrDefault(PRIORITY, 0);
    }
    static Map<Class<?>, Function<String, Object>> initConvertors() {
        Map<Class<?>, Function<String, Object>> convertors = new HashMap<>();
        convertors.put(int.class,Integer::valueOf);
        convertors.put(Integer .class,Integer::valueOf);
        convertors.put(boolean.class,Boolean::valueOf);
        convertors.put(Boolean .class,Boolean::valueOf);
        convertors.put(String .class,(s)->s);
        return convertors;
    }

    static Map<String, Setter> initPropertySetter() {
        HashMap<String, Setter> res = new HashMap<>();
        Class<Config.ConfigBuilder> configBuilderClass = Config.ConfigBuilder.class;
        Method[] methods = configBuilderClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameterCount() == 1 && method.getModifiers() == Modifier.PUBLIC) {
                Class<?> parameterType = method.getParameterTypes()[0];
                Function<String, Object> converter = convertors.get(parameterType);
                if (converter != null) {
                    res.put(method.getName(), new Setter(converter, method));
                }
            }
        }
        return res;
    }

    @Value
    static class Setter {
        Function<String, Object> converter;
        Method method;

        void apply(Config.ConfigBuilder builder, String value) {
            try {
                method.invoke(builder, converter.apply(value));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InitializationFailure("Can't convert property" + method.getName(), e);
            }
        }
    }
}

