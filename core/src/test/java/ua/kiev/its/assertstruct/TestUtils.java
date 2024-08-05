package ua.kiev.its.assertstruct;

import lombok.experimental.UtilityClass;
import ua.kiev.its.assertstruct.result.MatchResult;
import ua.kiev.its.assertstruct.template.Template;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@UtilityClass
public class TestUtils {
    /**
     * Creates a new LinkedHashMap with the given key-value pairs.
     *
     * @param  pairs  an array of objects representing key-value pairs
     * @return        a new LinkedHashMap with the given key-value pairs
     */
    public static Map mapOf(Object ... pairs) {
        LinkedHashMap map = new LinkedHashMap();
        for (int i = 0; i < pairs.length; i += 2) {
            map.put(pairs[i], pairs[i + 1]);
        }
        return map;
    }
    public static List listOf(Object ... values) {
        return Arrays.asList(values);
    }
    public static void checkFail(String templatePath, String expectedPath, Object actualValue) {
        checkFail(Res.from(templatePath), Res.from(expectedPath), actualValue);
    }
    public static void checkFail(Res resTemplate, Res resExpected, Object actualValue) {
        Template template = parse(resTemplate);
        MatchResult match = template.match(actualValue);
        assertEquals(resExpected.asString(), match.asString());
        assertTrue(match.hasDifference());
    }

    public static void checkOK(String tempalePath, Object actualValue) {
        checkOK(Res.from(tempalePath), actualValue);
    }
    public static void checkOK(Res res, Object actualValue) {
        Template template = res.asTemplate();
        MatchResult match = template.match(actualValue);
        assertEquals(template.asString(), match.asString());
        assertFalse(match.hasDifference());
    }

    public static Template parse(Res res) {
        return res.asTemplate();
    }

}
