package org.assertstruct.impl.parser;

import org.assertstruct.Res;
import org.assertstruct.template.StructTemplateShared;
import org.assertstruct.template.Template;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

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
    public static final String[] STRUCT_TEMPLATE_FIELDS = {
            "inline", "trailingComa", "defaultIndent", "firstInlineElementIndent", "inlineElementsSeparator"
    };
    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            " true;  false;  ;   0;   ;  {a: 0}",
            " true;  false;  ;   0;  1;  {a: 0, b: 1}",
            " true;  false;  ;   2;  3;  {  a: 0,   b: 1}",
            "false;   true; 2;    ;   ;  {>  a: 0,>  b: 1,}",
            " true;  false;  ;   0;   ;  [0]",
            " true;  false;  ;   0;  1;  [0, 1]",
            " true;  false;  ;   0;  1;  [1, 3, '$...', 7]",
            " true;  false;  ;   2;  3;  [  0,   1]",
            "false;   true; 2;    ;   ;  [>  0,>  1,]",
    })
    void testParseFormattingDictInline(boolean inline, Boolean trailingComa, Integer defaultIndent, Integer firstInlineElementIndent, Integer inlineElementsSeparator,  String templateValue) throws IOException {
        Template template = Res.of(templateValue.replace(">", "\n")).asTemplate();
        StructTemplateShared dict = (StructTemplateShared) template.getRoot();
        assertThat(dict)
                .as("template: "+templateValue)
                .extracting(STRUCT_TEMPLATE_FIELDS)
                .containsExactly(inline, trailingComa, defaultIndent, firstInlineElementIndent, inlineElementsSeparator);
    }

}