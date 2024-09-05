package org.assertstruct.impl.factories.spel;

import org.assertstruct.Res;
import org.assertstruct.template.Template;
import org.assertstruct.template.TemplateNode;
import org.assertstruct.utils.MapUtils;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.HashMap;

import static org.assertstruct.AssertStruct.*;
import static org.junit.jupiter.api.Assertions.*;

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
                () -> assertStruct("{ '#size()': 0 }", MapUtils.mapOf("key", "value")));
        assertEquals("{ '#size()': 1, key: 'value' }", assertionFailedError.getActual().getValue());
    }

}