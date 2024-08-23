package org.assertstruct.utils;

import lombok.experimental.UtilityClass;

import java.util.Scanner;

@UtilityClass
public class ResourceUtils {
    /**
     * Reads resource as String from classpath
     *
     * @param resource
     * @return
     */
    public static String resAsStr(String resource) {
        try {
            return new Scanner(ResourceUtils.class.getClassLoader().getResourceAsStream(resource), "UTF-8").useDelimiter("\\A").next();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static StackTraceElement codeLocator() {
        return new Throwable().getStackTrace()[2];
    }


}
