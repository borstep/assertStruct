package org.assertstruct.utils;

import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.Map;

@UtilityClass
public class MapUtils {
    /**
     * Creates a new LinkedHashMap with the given key-value pairs.
     *
     * @param  pairs  an array of objects representing key-value pairs
     * @return        a new LinkedHashMap with the given key-value pairs
     */
    public static Map<?,?> mapOf(Object ... pairs) {
        LinkedHashMap<Object, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            map.put(pairs[i], pairs[i + 1]);
        }
        return map;
    }
}
