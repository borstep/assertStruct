package org.assertstruct.converter;

import org.assertstruct.template.TemplateKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ListWrapper extends ArrayList<Object> implements JsonStruct, Wrapper<Object, List<Object>> {
    private final Object source;

    public ListWrapper(Object source) {
        this.source = source;
    }

    public ListWrapper(Collection source) {
        super(source);
        this.source = source;
    }

    @Override
    public void addChild(String key, Object value) {
        add(value);
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public List<Object> getValue() {
        return this;
    }

    @Override
    public Object getChild(Object key) {
        if (key instanceof TemplateKey) {
            key = ((TemplateKey) key).getValue();
        }
        if (!(key instanceof Number)) {
            throw new IllegalArgumentException("List wrapper does not support key '"+key+"' ::"+key.getClass());
        }
        return get(((Number) key).intValue());
    }

    @Override
    public void setChild(Object key, Object value) {
        if (key instanceof TemplateKey) {
            key = ((TemplateKey) key).getValue();
        }
        if (!(key instanceof Number)) {
            throw new IllegalArgumentException("List wrapper does not support key '"+key+"' ::"+key.getClass());
        }
        set(((Number) key).intValue(), value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListWrapper)) return false;
        if (!super.equals(o)) return false;
        ListWrapper objects = (ListWrapper) o;
        return Objects.equals(source, objects.source);
    }

}
