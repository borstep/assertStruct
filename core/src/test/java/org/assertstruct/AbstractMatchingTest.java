package org.assertstruct;

import lombok.RequiredArgsConstructor;
import org.assertstruct.result.MatchResult;
import org.assertstruct.service.AssertStructService;
import org.assertstruct.template.Template;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class AbstractMatchingTest {
    public final AssertStructService env;

    public void checkFail(String template, String expected, Object actualValue) {
        checkFail(Res.res(template), Res.res(expected), actualValue);
    }

    public void checkFail(Res resTemplate, Res resExpected, Object actualValue) {
        Template template = parse(resTemplate);
        MatchResult match = env.match(template, actualValue);
        assertEquals(resExpected.asString(), env.jsonify(match));
        assertTrue(match.hasDifference());
    }

    public void checkOK(String template, Object actualValue) {
        checkOK(Res.res(template), actualValue);
    }

    public void checkOK(Res res, Object actualValue) {
        Template template = parse(res);
        MatchResult match = env.match(template, actualValue);
        assertEquals(template.asString(), env.jsonify(match));
        assertFalse(match.hasDifference());
    }

    public Template parse(Res res) {
        return res.asTemplate(env);
    }

}
