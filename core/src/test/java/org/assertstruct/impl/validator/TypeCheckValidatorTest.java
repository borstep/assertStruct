package org.assertstruct.impl.validator;

import data.PolymorphInt;
import data.PolymorphString;
import data.TestPojo;
import data.ValueObject;
import org.assertstruct.Res;
import org.assertstruct.service.SharedValidator;
import org.assertstruct.template.Template;
import org.assertstruct.template.TemplateNode;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;
import java.util.Set;

import static org.assertstruct.AssertStruct.*;
import static org.assertstruct.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class TypeCheckValidatorTest {

    @Test
    void parse() {
        Template template = Res.of("'$* ::java.lang.String'").asTemplate();
        TemplateNode root = template.getRoot();
        Set<SharedValidator> validators = root.getValidators();
        assertEquals(
                listOf(new TypeCheckValidator(String.class)),
                new ArrayList<>(validators)
        );
    }

    @Test
    void parseShort() {
        Template template = Res.of("'$* ::String'").asTemplate();
        TemplateNode root = template.getRoot();
        Set<SharedValidator> validators = root.getValidators();
        assertEquals(
                listOf(new TypeCheckValidator(String.class)),
                new ArrayList<>(validators)
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

    @Test
    void matchPolymorphicTypeOK() {
        assertStruct("{polymorphes: [" +
                "{ type: 'PolymorphString', str: 'str' }," +
                "{ type: 'PolymorphInt', i: 1 }" +
                "]}", TestPojo.pojo()
                .polymorphe(new PolymorphString("str"))
                .polymorphe(new PolymorphInt(1))
                .build());
    }
}