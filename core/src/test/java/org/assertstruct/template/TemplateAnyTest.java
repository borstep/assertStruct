package org.assertstruct.template;

import org.junit.jupiter.api.Test;
import org.assertstruct.utils.MapUtils;

import java.io.IOException;

import static org.assertstruct.TestUtils.*;

class TemplateAnyTest {


    @Test
    void dictAnyValueOK() throws IOException {
        checkOK(
                "template/any/dict/anyValue.json5",
                MapUtils.mapOf("key", "value")
        );
    }

    @Test
    void dictAnyKeyOK() throws IOException {
        checkOK(
                "template/any/dict/anyKey.json5",
                MapUtils.mapOf("key", "value", "key1", "value")
        );
    }

    @Test
    void dictAnyKeyFail() throws IOException {
        checkFail(
                "template/any/dict/anyKey.json5",
                "template/any/dict/anyKey.wrongValue.json5",
                MapUtils.mapOf("key", "value", "key1", "value1")
        );
    }


}