package ua.kiev.its.assertstruct.template;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static ua.kiev.its.assertstruct.TestUtils.*;

class TemplateMatchKeyTest {

    @Test
    void matchSingleKeyOK() throws IOException {
        checkOK("template/simple/dict/match.json5", mapOf(
                "key", "value"
                , "keyMatch", "value2"
                , "key3", "value3"
        ));
    }

    @Test
    void matchMultiKeyOK() throws IOException {
        checkOK("template/simple/dict/match.json5", mapOf(
                "key", "value"
                , "keyMatch", "value2"
                , "key2Match", "value2"
                , "key3", "value3"
        ));
    }

    @Test
    void matchWrongOrderOK() throws IOException {
        checkOK("template/simple/dict/match.json5", mapOf(
                "keyMatch", "value2"
                , "key2Match", "value2"
                , "key", "value"
                , "key3", "value3"
        ));
    }

    @Test
    void matchOrderedOK() throws IOException {
        checkOK("template/simple/dict/matchOrdered.json5", mapOf(
                "key", "value"
                , "keyMatch", "value2"
                , "key2Match", "value2"
                , "key3", "value3"
        ));
    }

    @Test
    void matchOrderedFail() throws IOException {
        checkFail("template/simple/dict/matchOrdered.json5",
                "template/simple/dict/matchOrdered.wrongOrder.json5", mapOf(
                        "key", "value"
                        , "key3", "value3"
                        , "keyMatch", "value2"
                        , "key2Match", "value2"
                ));
    }

}