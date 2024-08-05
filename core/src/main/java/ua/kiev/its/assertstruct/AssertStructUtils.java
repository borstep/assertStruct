package ua.kiev.its.assertstruct;

import lombok.experimental.UtilityClass;

import static ua.kiev.its.assertstruct.utils.ResourceUtils.codeLocator;

@UtilityClass
public class AssertStructUtils {
    static AssertStruct defaultInstance = AssertStruct.buildDefault();

    public static AssertStruct getDefault() {
        return defaultInstance;
    }

    public static void assertStructMatch(String resource, Object actualValue) {

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

}
