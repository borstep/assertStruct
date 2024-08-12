package ua.kiev.its.assertstruct.impl.validator;

import data.TestPojo;
import data.ValueObject;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import ua.kiev.its.assertstruct.Res;
import ua.kiev.its.assertstruct.config.SharedValidator;
import ua.kiev.its.assertstruct.impl.factories.any.AnyNode;
import ua.kiev.its.assertstruct.impl.factories.variable.ConstantNode;
import ua.kiev.its.assertstruct.impl.factories.variable.matcher.AnyList;
import ua.kiev.its.assertstruct.template.Template;
import ua.kiev.its.assertstruct.template.TemplateNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ua.kiev.its.assertstruct.AssertStructUtils.assertStruct;
import static ua.kiev.its.assertstruct.TestUtils.listOf;

class TypeCheckValidatorTest {

    @Test
    void parse() {
        Template template = Res.of("'$* ::java.lang.String'").asTemplate();
        TemplateNode root = template.getRoot();
        Set<SharedValidator> validators = root.getValidators();
        assertEquals(
                listOf(new TypeCheckValidator(String.class)),
                new ArrayList(validators)
        );
    }

    @Test
    void parseShort() {
        Template template = Res.of("'$* ::String'").asTemplate();
        TemplateNode root = template.getRoot();
        Set<SharedValidator> validators = root.getValidators();
        assertEquals(
                listOf(new TypeCheckValidator(String.class)),
                new ArrayList(validators)
        );
    }

    @Test
    void matchOK() {
        assertStruct("'$* ::java.lang.String'", "some string");
    }

    @Test
    void matchFail() {
        assertThrows(AssertionFailedError.class,
                () -> assertStruct("'$* ::java.lang.Integer'", "some string"));
    }

    @Test
    void matchSourceTypeOK() {
        assertStruct("{ value:'str ::data.ValueObject' }", TestPojo.pojo().value(new ValueObject("str")).build());
    }
}