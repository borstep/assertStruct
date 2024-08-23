package org.assertstruct.impl.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.assertstruct.Res;

import java.io.IOException;

class JSon5ParserTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "simple",
            "dict",
            "overall"
    })
    void testParseResource(String resName) throws IOException {
        parse(resName);
    }

    private void parse(String resName) throws IOException {
        JSon5Parser parser = new JSon5Parser(Res.from("$$/"+resName+".json5").asChars());
        ExtToken token;
        StringBuilder sb = new StringBuilder();
        try {
            while ((token=parser.next()) != null) {
                token.printDebug(sb);
            }
        } catch (IOException e) {
            System.err.println(sb.toString());
            throw e;
        } catch (RuntimeException e) {
            System.err.println(sb.toString());
            throw e;
        }
        Assertions.assertEquals(Res.from("$$/"+resName+".result.txt").asString(), sb.toString());

    }


}