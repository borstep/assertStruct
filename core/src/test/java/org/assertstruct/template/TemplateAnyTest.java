package org.assertstruct.template;

import org.assertstruct.utils.MapUtils;
import org.junit.jupiter.api.Test;

import static org.assertstruct.TestUtils.*;

class TemplateAnyTest {


    @Test
    void dictAnyValueOK() {
        checkOK(
                "template/any/dict/anyValue.json5",
                MapUtils.mapOf("key", "value")
        );
    }

    @Test
    void dictAnyKeyOK() {
        checkOK(
                "template/any/dict/anyKey.json5",
                MapUtils.mapOf("key", "value", "key1", "value")
        );
    }

    @Test
    void dictAnyKeyFail() {
        checkFail(
                "template/any/dict/anyKey.json5",
                "template/any/dict/anyKey.wrongValue.json5",
                MapUtils.mapOf("key", "value", "key1", "value1")
        );
    }


}