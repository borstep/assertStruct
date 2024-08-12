package ua.kiev.its.assertstruct.converter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Wrapper<S, V> {
    @JsonIgnore
    S getSource();
    @JsonIgnore
    V getValue();

    default Wrapper getChildWrapper(Object key) {
        Object child = getChild(key);
        if (child instanceof Wrapper) {
            return (Wrapper) child;
        }
        throw new IllegalArgumentException("Child "+key+" is not a wrapper");
    }

    default Object getChildValue(Object key) {
        Object child = getChild(key);
        if (child instanceof Wrapper) {
            return ((Wrapper) child).getValue();
        }
        return child;
    }

    default Object getChildSource(Object key) {
        Object child = getChild(key);
        if (child instanceof Wrapper) {
            return ((Wrapper) child).getSource();
        }
        return child;
    }


    default Object getChild(Object key) {
        throw new IllegalArgumentException("Wrapper does not support children");
    }

    default void setChild(Object key, Object value) {
        throw new IllegalArgumentException("Wrapper does not support children");
    }

    default MapWrapper asMapWrapper(){
        if (this instanceof MapWrapper) {
            return (MapWrapper) this;
        }
        throw new IllegalArgumentException("Wrapper is not a Map");
    }

    default ListWrapper asListWrapper(){
        if (this instanceof ListWrapper) {
            return (ListWrapper) this;
        }
        throw new IllegalArgumentException("Wrapper is not a List");
    }

    public static Object unwrap(Object value) {
        if (value instanceof Wrapper){
            return ((Wrapper)value).getValue();
        }
        return value;
    }

    public static Object source(Object value) {
        if (value instanceof Wrapper){
            return ((Wrapper)value).getSource();
        }
        return value;
    }

}
