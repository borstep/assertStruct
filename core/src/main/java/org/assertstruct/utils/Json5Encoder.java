package org.assertstruct.utils;

import com.fasterxml.jackson.core.io.CharTypes;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class Json5Encoder {

    private static final char[] NULL = "null".toCharArray();

    private static final char[] HC = "0123456789ABCDEF".toCharArray();

    private static final int[] sInputCodesJsNames = CharTypes.getInputCodeLatin1JsNames();

    private static final int[] sOutputEscapes128;

    static {
        sOutputEscapes128 = Arrays.copyOf(CharTypes.get7BitOutputEscapes(), CharTypes.get7BitOutputEscapes().length);
        sOutputEscapes128['"'] = 0; // Quotes escaped depends on selected Quote character
        sOutputEscapes128['\''] = 0;
    }

    public static int encodeKey(Object keyObj, StringBuilder builder, char quote, boolean forceQuote) {
        int len = builder.length();
        if (keyObj == null) {
            appendNull(builder);
        } else {
            String key = keyObj.toString();
            boolean quoted = forceQuote || quoteRequired(key, quote);
            if (quoted) {
                builder.append(quote);
            }
            appendQuoted(builder, key, quote);
            if (quoted) {
                builder.append(quote);
            }
        }
        return builder.length() - len;
    }

    public static boolean quoteRequired(String key, char quote) {
        int len = key.length();
        for (int i = 0; i < len; ++i) {
            char c = key.charAt(i);
            if (c < sInputCodesJsNames.length && sInputCodesJsNames[c] != 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * Helper method for appending JSON-escaped version of contents
     * into specific {@link StringBuilder}, using default JSON specification
     * mandated minimum escaping rules.
     *
     * @param sb      Buffer to append escaped contents in
     * @param content Unescaped String value to append with escaping applied
     */
    public static void appendQuoted(StringBuilder sb, String content, char quote) {
        final int[] escCodes = sOutputEscapes128;
        int escLen = escCodes.length;
        for (int i = 0, len = content.length(); i < len; ++i) {
            char c = content.charAt(i);
            if (c == quote) {
                sb.append('\\');
                sb.append(quote);
                continue;
            } else if (c >= escLen || escCodes[c] == 0) {
                sb.append(c);
                continue;
            }
            sb.append('\\');
            int escCode = escCodes[c];
            if (escCode < 0) { // generic quoting (hex value)
                // The only negative value sOutputEscapes128 returns
                // is CharacterEscapes.ESCAPE_STANDARD, which mean
                // appendQuotes should encode using the Unicode encoding;
                // not sure if this is the right way to encode for
                // CharacterEscapes.ESCAPE_CUSTOM or other (future)
                // CharacterEscapes.ESCAPE_XXX values.

                // We know that it has to fit in just 2 hex chars
                sb.append('u');
                sb.append('0');
                sb.append('0');
                int value = c;  // widening
                sb.append(HC[value >> 4]);
                sb.append(HC[value & 0xF]);
            } else { // "named", i.e. prepend with slash
                sb.append((char) escCode);
            }
        }
    }

    public void appendNull(StringBuilder builder) {
        builder.append(NULL);
    }
}
