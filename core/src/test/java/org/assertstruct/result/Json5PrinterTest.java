package org.assertstruct.result;

import org.junit.jupiter.api.Test;
import org.assertstruct.Res;
import org.assertstruct.template.Template;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertstruct.AssertStruct.assertStruct;
import static org.assertstruct.TestUtils.listOf;
import static org.assertstruct.utils.MapUtils.mapOf;

class Json5PrinterTest {

    @Test
    void printSimpleMap() throws IOException {
        Json5Printer printer = new Json5Printer();
        String expected = "{\n  key: 'value'\n}";
        assertEquals(expected, printer.print(mapOf("key", "value")));
    }

    @Test
    void printSimpleArray() throws IOException {
        Json5Printer printer = new Json5Printer();
        String expected = "[\n  1,\n  'str',\n  true\n]";
        assertEquals(expected, printer.print(listOf(1, "str", true)));
    }

    @Test
    void print2LevelsMap() throws IOException {
        Json5Printer printer = new Json5Printer();
        String expected = "{\n" +
                "  dict: {\n" +
                "    key: 'value'\n" +
                "  },\n" +
                "  arr: [\n" +
                "    1,\n" +
                "    'str',\n" +
                "    true\n" +
                "  ]\n" +
                "}";
        assertEquals(expected, printer.print(
                mapOf("dict", mapOf("key", "value"),
                        "arr", listOf(1, "str", true))
        ));
    }

    static Res TWO_LEVEL_TEMPLATE = Res.of("{\n" +
            "  key: 'value',\n" +
            "  arr: [\n" +
            "    1,\n" +
            "  ],\n" +
            "  dict: {\n" +
            "    key: 'value'\n" +
            "  },\n" +
            "}");

    static String TWO_LEVEL_MATCH = "{\n" +
            "  key: 'wrong',\n" +
            "  arr: [\n" +
            "    1,\n" +
            "  ],\n" +
            "  dict: {\n" +
            "    key: 'value'\n" +
            "  },\n" +
            "}";

    @Test
    void print2LevelsErrorLevel1Result() throws IOException {
        Json5Printer printer = new Json5Printer();
        Template template = TWO_LEVEL_TEMPLATE.asTemplate();
        MatchResult match = template.match(mapOf("key", "wrong",
                "arr", listOf(1),
                "dict", mapOf("key", "value")
        ));
        assertEquals(TWO_LEVEL_MATCH, printer.print(match));
    }
}