package ua.kiev.its.assertstruct.impl.factories.variable.matcher;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import ua.kiev.its.assertstruct.Res;
import ua.kiev.its.assertstruct.impl.factories.variable.ConstantNode;
import ua.kiev.its.assertstruct.template.Template;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.kiev.its.assertstruct.AssertStruct.assertStruct;


class AnyListTest {

    @Test
    void parse() {
        Template template = Res.of("'$[*]'").asTemplate();
        assertEquals(new ConstantNode(AnyList.NAME, null, null), template.getRoot());
    }

    @Test
    void matchOK() {
        assertStruct("'$[*]'", new int[]{1, 2, 3});
    }

    @Test
    void matchFail() {
        assertThrows(AssertionFailedError.class,
                () -> assertStruct("'$[*]'", new HashMap<>()));
    }

}