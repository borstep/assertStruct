package org.assertstruct.impl.factories.spel;

import org.assertstruct.Res;
import org.assertstruct.template.Template;
import org.assertstruct.template.TemplateNode;
import org.assertstruct.utils.MapUtils;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.assertstruct.AssertStruct.*;
import static org.junit.jupiter.api.Assertions.*;

public class SpelNodeTest {
    @Test
    void parse() {
        Template template = Res.of("{ fields: '#size()+1' }").asTemplate();
        TemplateNode evalNode = template.getRoot().asDict().get("fields");
        assertInstanceOf(SpelNode.class, evalNode);
        assertEquals("size()+1", ((SpelNode) evalNode).getExpression().getExpressionString());
    }

    @Test
    void matchOK() {
        assertStruct("{ fields: '#size()+1' }", MapUtils.mapOf("fields", 2));
    }

    @Test
    void matchFail() {
        AssertionFailedError assertionFailedError = assertThrows(AssertionFailedError.class,
                () -> assertStruct("{ fields: '#size()+1' }", MapUtils.mapOf("fields", 3)));
        assertEquals("{ fields: 3 }", assertionFailedError.getActual().getValue());
    }

}