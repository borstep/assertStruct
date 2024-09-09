package org.assertstruct.template;

import org.assertstruct.Res;
import org.assertstruct.impl.opt.NodeOptions;
import org.assertstruct.impl.parser.JSon5Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TemplateParserTest {
    static final Res TEMPLATE_WITH_COMMENTS = Res.from("$$/templateWithComments.json5");
    static final Res TEMPLATE_SIMPLE = Res.from("$$/simple.json5");

    @Test
    void parseSimple() throws IOException {
        assertEquals(TEMPLATE_SIMPLE.asString(), parseToString(TEMPLATE_SIMPLE));
    }

    @Test
    void parseWithComments() throws IOException {
        assertEquals(TEMPLATE_WITH_COMMENTS.asString(), parseToString(TEMPLATE_WITH_COMMENTS));
    }

    @Test
    void parseAny() throws IOException {
        Res res = Res.from("template/any/dict/anyKey.json5");
        assertEquals(res.asString(), parseToString(res));
    }

    @ParameterizedTest
    @CsvSource(value = {
            // Dicts
            "template/config/orderedDictDefault.json5, null",
            "template/config/orderedDictTrue.json5, true",
            "template/config/orderedDictTrue.short.json5, true",
            //Arrays
            "template/config/orderedArrayDefault.json5, null",
            "template/config/orderedArrayFalse.json5, false",
    }, nullValues={"null"})
    void configOrdered(String res, Boolean expected) throws IOException {
        Template template = parse(res);
        assertEquals(expected, template.asStruct().getConfig().getOrdered());
    }

    @Test
    void fullConfigWithInheritance() throws IOException {
        Template template = parse("template/config/fullConfigWithInheritance.json5");

        assertEquals(new NodeOptions(
                false,
                false), template.asStruct().getConfig(), "Wrong root node");
        assertEquals(new NodeOptions(
                true,
                true), template.asDict().getByKey("child").asDict().getConfig(), "Wrong inherited node");
        assertEquals(new NodeOptions(
                true,
                false), template.asDict().get("childOverride").asDict().getConfig(), "Wrong inherited node with override");
    }

/*
    @ParameterizedTest
    void configUnordered(String res, boolean expected) throws IOException {
        Template template = parse("template/config/unorderedDict.json5");
        assertFalse(template.getRoot().getConfig().isOrdered());
    }
*/


    private static String parseToString(Res res) throws IOException {
        return parse(res).asString();
    }

    private static Template parse(String res) throws IOException {
        return parse(Res.from(res));
    }

    private static Template parse(Res res) throws IOException {
        TemplateParser parser = new TemplateParser();
        return parser.parse(new JSon5Parser(res.asChars()));
    }
}