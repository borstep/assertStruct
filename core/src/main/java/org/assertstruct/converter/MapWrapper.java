package org.assertstruct.converter;

import org.assertstruct.template.TemplateKey;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class MapWrapper extends LinkedHashMap<String, Object> implements JsonStruct, Wrapper<Object, Map<String, Object>> {
    Object source;

    public MapWrapper(Object source) {
        this.source = source;
    }

    public MapWrapper(Map<String, ?> source) {
        super(source);
        this.source = source;
    }

    @Override
    public void addChild(String key, Object value) {
        this.put(key, value);
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public Map<String, Object> getValue() {
        return this;
    }

    @Override
    public Object getChild(Object key) {
        if (key instanceof TemplateKey) {
            key = ((TemplateKey) key).getValue();
        }
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("Map wrapper does not support key '"+key+"' ::"+key.getClass());
        }
        return get(key);
    }

    @Override
    public void setChild(Object key, Object value) {
        if (key instanceof TemplateKey) {
            key = ((TemplateKey) key).getValue();
        }
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("Map wrapper does not support key '"+key+"' ::"+key.getClass());
        }
        put((String) key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapWrapper)) return false;
        if (!super.equals(o)) return false;
        MapWrapper objects = (MapWrapper) o;
        return Objects.equals(source, objects.source);
    }

}
