package org.assertstruct.utils;

import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@UtilityClass
public class MapUtils {

    /**
     * Creates a new unmodifiable LinkedHashMap with the given key-value pairs.
     *
     * @param pairs an array of objects representing key-value pairs
     * @return a new LinkedHashMap with the given key-value pairs
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> mapOf(K key, V value, Object... pairs) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        map.put(key, value);
        for (int i = 0; i < pairs.length; i += 2) {
            map.put((K) pairs[i], (V) pairs[i + 1]);
        }
        return Collections.unmodifiableMap(map);
    }

    public static Map<?, ?> mapOf() {
        return Collections.emptyMap();
    }
}
