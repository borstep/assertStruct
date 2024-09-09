package org.assertstruct;

import lombok.experimental.UtilityClass;
import org.assertstruct.service.AssertStructConfigLoader;
import org.assertstruct.service.AssertStructService;
import org.assertstruct.service.Config;
import org.opentest4j.AssertionFailedError;

import static org.assertstruct.utils.ResourceUtils.*;

@UtilityClass
public class AssertStruct {
    static AssertStructService defaultInstance = AssertStructConfigLoader.loadDefaultService();

    public static AssertStructService getDefault() {
        return defaultInstance;
    }

    public static void assertStruct(String expected, Object actualValue) throws AssertionFailedError{
        StackTraceElement el = codeLocator();
        Res res = Res.res(expected, el, getDefault());
        getDefault().assertStruct(res, actualValue, null, el);
    }

    public static void assertStruct(String expected, Object actualValue, String message) throws AssertionFailedError {
        StackTraceElement el = codeLocator();
        Res res = Res.res(expected, el, getDefault());
        getDefault().assertStruct(res, actualValue, message, el);
    }


    public static void assertStruct(Res expectedTemplate, Object actualValue) throws AssertionFailedError {
        getDefault().assertStruct(expectedTemplate, actualValue, null, codeLocator());
    }

    public static void assertStruct(Res expectedTemplate, Object actualValue, String message) throws AssertionFailedError {
        getDefault().assertStruct(expectedTemplate, actualValue, message, codeLocator());
    }

    public static String jsonify(Object value) {
        return getDefault().jsonify(value);
    }

    public static Config.ConfigBuilder with() {
        return getDefault().with();
    }

    /**
     * Set default AssertStructService.
     * @param instance new default
     *
     * This is not thread-safe operation for performance reason. Use with care.
     */
    public static void setDefault(AssertStructService instance) {
        defaultInstance = instance;
    }

}
