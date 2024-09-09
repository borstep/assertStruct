package org.assertstruct;

import lombok.experimental.UtilityClass;
import org.assertstruct.template.Template;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class TestUtils {
    static AbstractMatchingTest env = new AbstractMatchingTest(AssertStruct.getDefault());

    @SafeVarargs
    public static <T> List<T> listOf(T... values) {
        return Arrays.asList(values);
    }

    public static void checkFail(String template, String expected, Object actualValue) {
        env.checkFail(Res.res(template), Res.res(expected), actualValue);
    }

    public static void checkFail(Res resTemplate, Res resExpected, Object actualValue) {
        env.checkFail(resTemplate, resExpected, actualValue);
    }

    public static void checkOK(String template, Object actualValue) {
        env.checkOK(Res.res(template), actualValue);
    }

    public static void checkOK(Res res, Object actualValue) {
        env.checkOK(res, actualValue);
    }

    public static Template parse(Res res) {
        return env.parse(res);
    }

}
