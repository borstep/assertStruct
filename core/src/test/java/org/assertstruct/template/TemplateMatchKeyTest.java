package org.assertstruct.template;

import org.assertstruct.utils.MapUtils;
import org.junit.jupiter.api.Test;

import static org.assertstruct.TestUtils.*;

class TemplateMatchKeyTest {

    @Test
    void matchSingleKeyOK() {
        checkOK("template/simple/dict/match.json5", MapUtils.mapOf(
                "key", "value"
                , "keyMatch", "value2"
                , "key3", "value3"
        ));
    }

    @Test
    void matchMultiKeyOK() {
        checkOK("template/simple/dict/match.json5", MapUtils.mapOf(
                "key", "value"
                , "keyMatch", "value2"
                , "key2Match", "value2"
                , "key3", "value3"
        ));
    }

    @Test
    void matchWrongOrderOK() {
        checkOK("template/simple/dict/match.json5", MapUtils.mapOf(
                "keyMatch", "value2"
                , "key2Match", "value2"
                , "key", "value"
                , "key3", "value3"
        ));
    }

    @Test
    void matchOrderedOK() {
        checkOK("template/simple/dict/matchOrdered.json5", MapUtils.mapOf(
                "key", "value"
                , "keyMatch", "value2"
                , "key2Match", "value2"
                , "key3", "value3"
        ));
    }

    @Test
    void matchOrderedFail() {
        checkFail("template/simple/dict/matchOrdered.json5",
                "template/simple/dict/matchOrdered.wrongOrder.json5", MapUtils.mapOf(
                        "key", "value"
                        , "key3", "value3"
                        , "keyMatch", "value2"
                        , "key2Match", "value2"
                ));
    }

    @Test
    void matchEmptyFail() {
        checkFail("{ key: 'value' }",
                "{ }",
                MapUtils.mapOf());
    }

}