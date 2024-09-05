package org.assertstruct.template;

import org.assertstruct.Res;
import org.assertstruct.utils.MapUtils;
import org.junit.jupiter.api.Test;

import static org.assertstruct.TestUtils.*;

class TemplateDictTest {
    public static final Object DICT = MapUtils.mapOf(
            "key", "value",
            "key1", "value1"
            );

    @Test
    void emptyOK() {
        checkOK(Res.of("{}"), MapUtils.mapOf());
    }

    @Test
    void singleOK() {
        checkOK(Res.of("{a:1}"), MapUtils.mapOf("a", 1));
    }

    @Test
    void singleFail() {
        checkFail(Res.of("{a:1}"), Res.of("{a:2}"), MapUtils.mapOf("a", 2));
    }

    @Test
    void doubleFail() {
        checkFail(Res.of("{a:1, b:2}"),Res.of("{a:2, b:3}"), MapUtils.mapOf("a", 2, "b", 3));
    }

    @Test
    void simpleOK() {
        checkOK("template/simple/dict/simple.json5", DICT);
    }


    @Test
    void simpleFailWrongValue() {
        checkFail(
                "template/simple/dict/simple.json5",
                "template/simple/dict/simple.wrongValue.json5",
                MapUtils.mapOf("key", "otherValue", "key1", "value1")
        );
    }

    @Test
    void simpleFailWrongKey() {
        checkFail(
                "template/simple/dict/simple.json5",
                "template/simple/dict/simple.wrongKey.json5",
                MapUtils.mapOf("wrongKey", "value", "key1", "value1")
        );
    }

    @Test
    void simpleFailAddKey() {
        checkFail(
                "template/simple/dict/simple.json5",
                "template/simple/dict/simple.missedKey.json5",
                MapUtils.mapOf(
                        "addKeyBefore", "otherValue",
                        "key", "value",
                        "addKeyAfter", "otherValue",
                         "key1", "value1"
                )
        );
    }

    @Test
    void orderedOK() {
        checkOK("template/simple/dict/ordered.json5", DICT);
    }
    @Test
    void orderedFailWrongOrder() {
        checkFail("template/simple/dict/ordered.json5",
                "template/simple/dict/ordered.wrongOrder.json5", MapUtils.mapOf(
                        "key1", "value1",
                        "key", "value"
                ));
    }

    @Test
    void simpleIgnoreUnknownOK() {
        checkOK("template/simple/dict/simpleIgnoreUnknown.json5", MapUtils.mapOf(
                "unknownKey", "value",
                "key", "value",
                "key1", "value1",
                "unknownKey2", "value"
        ));
    }
}