package org.assertstruct.impl.factories.variable.matcher;

import org.assertstruct.Res;
import org.assertstruct.impl.factories.variable.ConstantNode;
import org.assertstruct.template.Template;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.assertstruct.AssertStruct.*;
import static org.assertstruct.utils.MapUtils.*;
import static org.junit.jupiter.api.Assertions.*;


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