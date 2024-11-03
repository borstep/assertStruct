package org.assertstruct.result;

import org.assertstruct.AssertStruct;
import org.assertstruct.Res;
import org.assertstruct.template.Template;
import org.junit.jupiter.api.Test;

import static org.assertstruct.TestUtils.*;
import static org.assertstruct.utils.MapUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class Json5PrinterTest {

    @Test
    void printSimpleMap() {
        Json5Printer printer = new Json5Printer();
        String expected = "{\n" +
                "  key: 'value'\n" +
                "}";
        assertEquals(expected, printer.print(mapOf("key", "value")));
    }

    @Test
    void printSimpleMapInline() {
        Json5Printer printer = new Json5Printer(AssertStruct.getDefault().with().inlineContainers(true).configure());
        String expected = "{key: 'value', key1: 'value1'}";
        assertEquals(expected, printer.print(mapOf("key", "value", "key1", "value1")));
    }

    @Test
    void printSimpleArray() {
        Json5Printer printer = new Json5Printer();
        String expected = "[\n" +
                "  1,\n" +
                "  'str',\n" +
                "  true\n" +
                "]";
        assertEquals(expected, printer.print(listOf(1, "str", true)));
    }

    @Test
    void printSimpleArrayInline() {
        Json5Printer printer = new Json5Printer(AssertStruct.getDefault().with().inlineContainers(true).configure());
        String expected = "[1, 'str', true]";
        assertEquals(expected, printer.print(listOf(1, "str", true)));
    }

    @Test
    void print2LevelsMap() {
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
    void print2LevelsErrorLevel1Result() {
        Json5Printer printer = new Json5Printer();
        Template template = TWO_LEVEL_TEMPLATE.asTemplate();
        MatchResult match = AssertStruct.match(template,
                mapOf("key", "wrong",
                        "arr", listOf(1),
                        "dict", mapOf("key", "value")
                ));
        assertEquals(TWO_LEVEL_MATCH, printer.print(match));
    }

    @Test
    void arraySingleLineWrongFirst() {
        checkFail("[1, 2, 3]", "[0, 2, 3]", listOf(0, 2, 3));
    }

    @Test
    void arraySingleLineMissingLast() {
        checkFail("[1, 2, 3]", "[1, 2,]", listOf(1, 2));
    }

    @Test
    void arraySingleLineMissingFirst() {
        checkFail("[1, 2, 3, '$opt.ordered:false']", "[ 2, 3, '$opt.ordered:false']", listOf(2, 3));
    }

}
