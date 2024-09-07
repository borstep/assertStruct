package org.assertstruct;

import lombok.experimental.UtilityClass;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.result.MatchResult;
import org.assertstruct.template.Template;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@UtilityClass
public class TestUtils {
    @SafeVarargs
    public static <T> List<T> listOf(T ... values) {
        return Arrays.asList(values);
    }
    public static void checkFail(String template, String expected, Object actualValue) {
        checkFail(Res.res(template), Res.res(expected), actualValue);
    }
    public static void checkFail(Res resTemplate, Res resExpected, Object actualValue) {
        Template template = parse(resTemplate);
        MatchResult match = new Matcher(AssertStruct.getDefault(), template).match(actualValue);
        assertEquals(resExpected.asString(), AssertStruct.jsonify(match));
        assertTrue(match.hasDifference());
    }

    public static void checkOK(String template, Object actualValue) {
        checkOK(Res.res(template), actualValue);
    }
    public static void checkOK(Res res, Object actualValue) {
        Template template = res.asTemplate();
        MatchResult match = new Matcher(AssertStruct.getDefault(), template).match(actualValue);
        assertEquals(template.asString(), AssertStruct.jsonify(match));
        assertFalse(match.hasDifference());
    }

    public static Template parse(Res res) {
        return res.asTemplate();
    }

}
