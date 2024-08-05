package ua.kiev.its.assertstruct.template;

import org.junit.jupiter.api.Test;
import ua.kiev.its.assertstruct.Res;

import java.io.IOException;

import static ua.kiev.its.assertstruct.TestUtils.*;

class TemplateDictTest {
    public static final Object DICT = mapOf(
            "key", "value",
            "key1", "value1"
            );
    public static final Object ARRAY = listOf("a", "b", "c");


    @Test
    void emptyOK() throws IOException {
        checkOK(Res.of("{}"), mapOf());
    }

    @Test
    void singleOK() throws IOException {
        checkOK(Res.of("{a:1}"), mapOf("a", 1));
    }

    @Test
    void singleFail() throws IOException {
        checkFail(Res.of("{a:1}"), Res.of("{a:2}"), mapOf("a", 2));
    }

    @Test
    void doubleFail() throws IOException {
        checkFail(Res.of("{a:1, b:2}"),Res.of("{a:2, b:3}"), mapOf("a", 2, "b", 3));
    }

    @Test
    void simpleOK() throws IOException {
        checkOK("template/simple/dict/simple.json5", DICT);
    }


    @Test
    void simpleFailWrongValue() throws IOException {
        checkFail(
                "template/simple/dict/simple.json5",
                "template/simple/dict/simple.wrongValue.json5",
                mapOf("key", "otherValue", "key1", "value1")
        );
    }

    @Test
    void simpleFailWrongKey() throws IOException {
        checkFail(
                "template/simple/dict/simple.json5",
                "template/simple/dict/simple.wrongKey.json5",
                mapOf("wrongKey", "value", "key1", "value1")
        );
    }

    @Test
    void simpleFailAddKey() throws IOException {
        checkFail(
                "template/simple/dict/simple.json5",
                "template/simple/dict/simple.missedKey.json5",
                mapOf(
                        "addKeyBefore", "otherValue",
                        "key", "value",
                        "addKeyAfter", "otherValue",
                         "key1", "value1"
                )
        );
    }

    @Test
    void orderedOK() throws IOException {
        checkOK("template/simple/dict/ordered.json5", DICT);
    }
    @Test
    void orderedFailWrongOrder() throws IOException {
        checkFail("template/simple/dict/ordered.json5",
                "template/simple/dict/ordered.wrongOrder.json5", mapOf(
                        "key1", "value1",
                        "key", "value"
                ));
    }

    @Test
    void simpleIgnoreUnknownOK() throws IOException {
        checkOK("template/simple/dict/simpleIgnoreUnknown.json5", mapOf(
                "unknownKey", "value",
                "key", "value",
                "key1", "value1",
                "unknownKey2", "value"
        ));
    }
}