package org.assertstruct;

import lombok.experimental.UtilityClass;
import org.assertstruct.service.AssertStructConfigLoader;
import org.assertstruct.service.AssertStructService;
import org.assertstruct.service.Config;

import static org.assertstruct.utils.ResourceUtils.codeLocator;

@UtilityClass
public class AssertStruct {
    static AssertStructService defaultInstance = AssertStructConfigLoader.loadDefaultService();

    public static AssertStructService getDefault() {
        return defaultInstance;
    }

    public static void assertStruct(String expected, Object actualValue) {
        StackTraceElement el = codeLocator();
        Res res = Res.res(expected, el, getDefault());
        getDefault().match(res, actualValue, null, el);
    }

    public static void assertStruct(String expected, Object actualValue, String message) {
        StackTraceElement el = codeLocator();
        Res res = Res.res(expected, el, getDefault());
        getDefault().match(res, actualValue, message, el);
    }


    public static void assertStruct(Res expectedTemplate, Object actualValue) {
        getDefault().match(expectedTemplate, actualValue, null, codeLocator());
    }

    public static void assertStruct(Res expectedTemplate, Object actualValue, String message) {
        getDefault().match(expectedTemplate, actualValue, message, codeLocator());
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
