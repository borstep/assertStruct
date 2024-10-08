package org.assertstruct.impl.parser;

import org.assertstruct.Res;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

class JSon5ParserTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "simple",
            "dict",
            "overall"
    })
    void testParseResource(String resName) throws IOException {
        try (JSon5Parser parser = new JSon5Parser(Res.from("$$/"+resName+".json5").asChars())) {
            ExtToken token;
            StringBuilder sb = new StringBuilder();
            try {
                while ((token = parser.next()) != null) {
                    token.printDebug(sb);
                }
            } catch (IOException e) {
                System.err.println(sb);
                throw e;
            }
            Assertions.assertEquals(Res.from("$$/"+resName+".result.txt").asString(), sb.toString());
        }
    }


}