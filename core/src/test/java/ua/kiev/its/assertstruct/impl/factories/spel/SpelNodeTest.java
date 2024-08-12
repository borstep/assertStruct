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
        assertStruct("{ fields: '#size()+1' }", mapOf("fields", 2));
    }

    @Test
    void matchFail() {
        AssertionFailedError assertionFailedError = assertThrows(AssertionFailedError.class,
                () -> assertStruct("{ fields: '#size()+1' }", mapOf("fields", 3)));
        assertEquals("{ fields: 3 }", assertionFailedError.getActual().getValue());
    }

}