package ua.kiev.its.assertstruct.template;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static ua.kiev.its.assertstruct.TestUtils.*;

class TemplateAnyTest {


    @Test
    void dictAnyValueOK() throws IOException {
        checkOK(
                "template/any/dict/anyValue.json5",
                mapOf("key", "value")
        );
    }

    @Test
    void dictAnyKeyOK() throws IOException {
        checkOK(
                "template/any/dict/anyKey.json5",
                mapOf("key", "value", "key1", "value")
        );
    }

    @Test
    void dictAnyKeyFail() throws IOException {
        checkFail(
                "template/any/dict/anyKey.json5",
                "template/any/dict/anyKey.wrongValue.json5",
                mapOf("key", "value", "key1", "value1")
        );
    }


}