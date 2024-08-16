package ua.kiev.its.assertstruct;


import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.impl.parser.JSon5Parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static ua.kiev.its.assertstruct.utils.ResourceUtils.resAsStr;

public class Main {
    public static void main(String[] args) throws IOException {
    }

    public static void main2(String[] args) throws IOException {
        URL resource = Main.class.getClassLoader().getResource("examples/json5.json5");
        System.out.println(resource);
        InputStream x = resource.openStream();

        System.out.println(x);
        System.out.println(x.available());
        byte[] bytes = new byte[x.available()];
        x.read(bytes);
        String content = new String(bytes, StandardCharsets.UTF_8);
//        return content.toCharArray();

//        String content = new String(bytes, StandardCharsets.UTF_8);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(x, StandardCharsets.UTF_8));
//        StandardCharsets.UTF_8.newDecoder().
//        reader.read()

//        parseJson5Research();
//        parseJson5();
    }

    public static final void parseJson5() throws IOException {
        JSon5Parser parser = new JSon5Parser(resAsStr("examples/json5.json5").toCharArray());
        ExtToken token = parser.next();
        StringBuilder sb = new StringBuilder();
        while (token != null) {
            System.out.println(token);
            token.printDebug(sb);
            token = parser.next();
        }
        System.out.println("\n\n------------------------------------\n");
        System.out.println(sb.toString());
    }

    public static final void parseJson5Research() throws IOException {
        JsonFactory factory = JsonFactory.builder()
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
        String content = resAsStr("examples/json5.json5");
        System.out.println(content);
        JsonParser parser = factory.createParser(content);
        JsonToken jsonToken;
        parser.close();
        while ((jsonToken = parser.nextToken()) != null) {
            parser.finishToken();

            JsonLocation currentTokenLocation = parser.currentTokenLocation();
            JsonLocation currentLocation = parser.currentLocation();
            System.out.println(String.format("%-20s %-15s %-15s %-3d:%3d `%s"
                    , jsonToken
                    , parser.getText()
                    , content.substring((int) currentTokenLocation.getCharOffset(), (int) currentLocation.getCharOffset())
                    , currentTokenLocation.getCharOffset()
                    , currentLocation.getCharOffset()
                    , currentLocation.offsetDescription()));
        }

        parser.close();
    }
}