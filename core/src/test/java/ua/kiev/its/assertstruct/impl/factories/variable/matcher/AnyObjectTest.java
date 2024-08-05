package ua.kiev.its.assertstruct.impl.factories.variable.matcher;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import ua.kiev.its.assertstruct.Res;
import ua.kiev.its.assertstruct.impl.factories.variable.ConstantNode;
import ua.kiev.its.assertstruct.template.Template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.kiev.its.assertstruct.AssertStructUtils.assertStruct;
import static ua.kiev.its.assertstruct.TestUtils.mapOf;


class AnyObjectTest {

    @Test
    void parse() {
        Template template = Res.of("'${*}'").asTemplate();
        assertEquals(new ConstantNode(AnyObject.NAME, null, null), template.getRoot());
    }

    @Test
    void matchOK() {
        assertStruct("'${*}'", mapOf("a", 1, "b", 2, "c", 3));
    }

    @Test
    void matchFail() {
        assertThrows(AssertionFailedError.class,
                () -> assertStruct("'${*}'", new int[]{1, 2, 3}));
    }

}