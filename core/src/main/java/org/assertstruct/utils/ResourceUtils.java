package org.assertstruct.utils;

import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.Scanner;

@UtilityClass
public class ResourceUtils {
    /**
     * Reads resource as String from classpath. Useful for loading templates from classpath
     *
     * @param resource resource path. Can be absolute or relative to the class executed from method
     * @return content of the resource
     */
    public static String resAsStr(String resource) {
        return new Scanner(Objects.requireNonNull(ResourceUtils.class.getClassLoader().getResourceAsStream(resource)), "UTF-8").useDelimiter("\\A").next();
    }


    /**
     * @return StackTraceElement of the caller
     */
    public static StackTraceElement codeLocator() {
        return new Throwable().getStackTrace()[2];
    }


}
