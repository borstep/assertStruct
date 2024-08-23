package org.assertstruct.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.assertstruct.impl.factories.any.ShortAnyParser;
import org.assertstruct.impl.factories.array.RepeaterParser;
import org.assertstruct.impl.factories.date.DateFactory;
import org.assertstruct.impl.factories.regexp.RegexpParser;
import org.assertstruct.impl.factories.spel.SpelFactory;
import org.assertstruct.impl.factories.variable.ConstantParser;
import org.assertstruct.impl.opt.OptionsParser;

import java.util.Arrays;
import java.util.List;

@UtilityClass
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class ConfigDefaults {
    static List<ParserFactory> DEFAULT_PARSER_FACTORIES = Arrays.asList(
            ConstantParser.INSTANCE,
            RepeaterParser.INSTANCE,
            OptionsParser.INSTANCE,
            ShortAnyParser.INSTANCE,
            RegexpParser.INSTANCE,
            SpelFactory.INSTANCE,
            DateFactory.INSTANCE
    );

    static List<String> DEFAULT_SRC_PATHS = Arrays.asList(
            "/src/main/java/",
            "/src/main/resources/",
            "/src/test/java/",
            "/src/test/resources/"
    );
    static List<String> DEFAULT_TARGET_PATHS = Arrays.asList(
            "/target/classes/",
            "/target/test-classes/",
            "/out/classes/",
            "/out/test-classes/"
    );
    static List<String> DEFAULT_PACKAGES = Arrays.asList(
            "java.lang",
            "java.util",
            "java.time");

    public static JsonFactory buildDefaultJson5Factory() {
        return JsonFactory.builder()
                .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
                .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
                .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
                .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
                .enable(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS)
                .enable(JsonReadFeature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS)
                .enable(JsonReadFeature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS)
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();
    }

}
