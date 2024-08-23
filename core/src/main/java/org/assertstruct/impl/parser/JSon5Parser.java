package org.assertstruct.impl.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.assertstruct.AssertStruct;
import org.assertstruct.service.AssertStructService;
import org.assertstruct.service.Config;

import java.io.Closeable;
import java.io.IOException;

public class JSon5Parser implements Closeable {
    private AssertStructService env;
    Config config;

    private char[] source;
    //    private Stack<TemplateToken> stack = new Stack<>();
//    private int lastInlineCommentStart = -1;
    private ExtToken lastToken = null;
    private boolean requireInit = true;
    JsonParser parser = null;

    public JSon5Parser(char[] source) {
        this(source, AssertStruct.getDefault());
    }

    public JSon5Parser(char[] source, AssertStructService env) {
        this.source = source;
        this.env = env;
        this.config = env.getConfig();
    }

    private void init() throws IOException {
        JsonFactory json5Factory = config.getJson5Factory();
        parser = json5Factory.createParser(source);
        requireInit = false;
    }

    public ExtToken next() throws IOException {
        if (requireInit) {
            init();
        } else if (parser == null) { // parser is closed
            return null;
        }
        JsonToken jsonToken = parser.nextToken();
        parser.finishToken();
        if (jsonToken == null) {
            if (lastToken == null) {
                return null;
            } else {
                lastToken.setSuffix(source.length - 1);
                ExtToken tokenToReturn = lastToken;
                lastToken = null;
                return tokenToReturn;
            }
        }

        int startPosition = (int) parser.currentTokenLocation().getCharOffset();
        int endPosition = (int) parser.currentLocation().getCharOffset() - 1;
        Object value = null;
        switch (jsonToken) {
            case FIELD_NAME:
                value = parser.currentName();
                break;
            case VALUE_STRING:
                value = parser.getText();
                break;
            case VALUE_NUMBER_FLOAT:
            case VALUE_NUMBER_INT:
                value = parser.getNumberValue();
                break;
            case VALUE_TRUE:
                value = true;
                break;
            case VALUE_FALSE:
                value = false;
                break;
            case VALUE_EMBEDDED_OBJECT:
                value = parser.getEmbeddedObject();
                break;
            default:
                value = null;
                break;
        }
        ExtToken current = new ExtToken(source, startPosition, endPosition, jsonToken, value, parser.currentTokenLocation());

        if (lastToken == null) { // extend prev token
            current.setPrefix(0);
            lastToken = current;
            return next();
        } else if (lastToken.getEnd() > startPosition - 1) { // extend prev token
            lastToken.setEnd(startPosition - 1);
        }
        int prevPosition = lastToken.getEnd() + 1;
        // scan for suffix of previous token
        int firstLine = -1;
        while (true) {
            int idx = scan(prevPosition, startPosition);
            if (idx == -1) { // reach next token
                if (firstLine != -1) // didn't find any comma, so revert to first line position
                    prevPosition = firstLine + 1;
                lastToken.setSuffix(prevPosition - 1);
                current.setPrefix(prevPosition);
                break;
            } else if (source[idx] == '\n') { // reach EOL
                if (lastToken.isRequireComa()) {
                    if (firstLine == -1) // preserve first line position in case we didn't find any comma
                        firstLine = idx;
                    prevPosition = idx + 1;
                } else {
                    lastToken.setSuffix(idx);
                    current.setPrefix(idx + 1);
                    break;
                }
            } else if (source[idx] == ',') { // Coma found, but we need to check for trailing comment
                lastToken.setComaIncluded(true);
                prevPosition = idx + 1;
            } else { // Must never happen because Jackson already parsed and validated input
                throw new IllegalArgumentException("Unexpected char: " + source[idx] + " at position " + idx + " " + parser.currentLocation().offsetDescription());
            }
        }
        ExtToken tokenToReturn = lastToken;
        lastToken = current;
        return tokenToReturn;
    }


    /**
     * Scan source for the next non-whitespace token or EOL while skipping comments
     *
     * @param startIndex starting index must be less than endIndex
     * @param endIndex   ending index
     * @return index of the next non-whitespace token or -1 if not found.
     */
    private int scan(int startIndex, int endIndex) {
        ParseState state = ParseState.NONE;
        for (int i = startIndex; i < endIndex; i++) {
            if (state == ParseState.MULTILINE_COMMENT) {
                if (source[i] == '*' && source[i + 1] == '/') {
                    state = ParseState.NONE;
                    i++;
                    continue;
                }
            } else if (state == ParseState.IN_LINE_COMMENT) {
                if (source[i + 1] == '\n') {
                    state = ParseState.NONE;
                    continue;
                }
            } else {
                if (source[i] == '/' && source[i + 1] == '*') {
                    state = ParseState.MULTILINE_COMMENT;
                    i++;
                    continue;
                } else if (source[i] == '/' && source[i + 1] == '/') {
                    state = ParseState.IN_LINE_COMMENT;
                    i++;
                    continue;
                } else if (source[i] == '\n' || !Character.isSpaceChar(source[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    public void close() throws IOException {
        if (parser != null) {
            parser.close();
            parser = null;
        }
    }

    public JsonLocation currentLocation() {
        return parser.currentLocation();
    }

}
