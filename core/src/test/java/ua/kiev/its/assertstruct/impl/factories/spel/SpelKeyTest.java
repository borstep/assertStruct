package ua.kiev.its.assertstruct.impl.factories.spel;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import ua.kiev.its.assertstruct.Res;
import ua.kiev.its.assertstruct.template.Template;
import ua.kiev.its.assertstruct.template.TemplateNode;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static ua.kiev.its.assertstruct.AssertStructUtils.*;
import static ua.kiev.its.assertstruct.TestUtils.*;

public class SpelKeyTest {
    @Test
    void parse() {
        Template template = Res.of("{ '#size()': 0 }").asTemplate();
        TemplateNode evalKeyNode = template.getRoot().asDict().get("#size()");
        assertInstanceOf(SpelKey.class, evalKeyNode.getKey());
        assertEquals("#size()", evalKeyNode.getKey().getValue());
        assertEquals("size()", ((SpelKey) evalKeyNode.getKey()).getExpression().getExpressionString());
    }

    @Test
    void matchOK() {
        assertStruct("{ '#size()': 0 }", new HashMap<>());
    }

    @Test
    void matchFail() {
        AssertionFailedError assertionFailedError = assertThrows(AssertionFailedError.class,
                () -> assertStruct("{ '#size()': 0 }", mapOf("key", "value")));
        assertEquals("{ '#size()': 1, key: 'value' }", assertionFailedError.getActual().getValue());
    }

}