package org.assertstruct.impl.parser;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter(AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExtToken {

    private final JsonLocation location;

    private char[] _source;
    /**
     * Index of first character of token prefix
     */
    private int prefix = -1;
    /**
     * Index of first character of token
     */
    private int start = -1;
    /**
     * Index of last character of token
     */
    private int end = -1;
    /**
     * Index of last character of token suffix
     */
    private int suffix = -1;
    private boolean comaIncluded;
    private int leadingSpaces = 0;
    private int indent = 0;
    private JsonToken type;
    private Object value;
    private char quoteChar = 0;
    private boolean fromNewLine = false;

    public ExtToken(char[] source, int start, int end, JsonToken type, Object value, JsonLocation location) {
        this._source = source;
        this.prefix = this.start = start;
        this.suffix = this.end = end;
        this.type = type;
        this.value = value;
        this.quoteChar = source[start];
        this.location = location;
    }

    private void calcSpaces() {
        for (int i = prefix; i < start; i++) {
            if (_source[i] == ' ') {
                leadingSpaces++;
            } else {
                break;
            }
        }
        indent = start - prefix;
        for (int i = start - 1; i >= prefix; i--) {
            if (_source[i] == '\n') {
                indent = start - i - 1;
                break;
            }
        }
        if (prefix == 0 || _source[prefix - 1] == '\n') {
            fromNewLine = true;
        }
    }

    public void setSuffix(int suffix) {
        this.suffix = suffix;
        calcSpaces();
    }

    public boolean isEOLIncluded() {
        return suffix != -1 && _source[suffix] == '\n';
    }

    public boolean isRequireComa() {
        return !comaIncluded && type.isScalarValue();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isDoubleQuoted() {
        return quoteChar == '"';
    }

    public void print(StringBuilder out, boolean forceComa, boolean forceEOL) {
        printPrefix(out);
        out.append(_source, start, end - start + 1);
        printSuffix(out, forceComa, forceEOL);
    }

    public void printDebug(StringBuilder out) {
        out.append('<');
        printPrefix(out);
        out.append('<');
        out.append(_source, start, end - start + 1);
        out.append('>');
        printSuffix(out, false, false);
        out.append('>');
    }

    public String toString() {
        return "TemplateToken([" + this.getPrefix() + ":" + this.getStart() + ":" + this.getEnd()
                + ":" + this.getSuffix() + "] " + this.getIndent() + " " + this.getType()
                + (type.isStructEnd() || type.isStructStart() ? "" : type == JsonToken.FIELD_NAME ? ": " + this.getValue() : ": '" + this.getValue() + "'")
                + (isComaIncluded() ? "," : "") + (isEOLIncluded() ? "\\n" : "")
                + " )";
    }

    public void printPrefix(StringBuilder out) {
        out.append(_source, prefix, start - prefix);
    }

    public void printSuffix(StringBuilder out, boolean forceComa, boolean forceEOL) {
        if (forceComa && !isComaIncluded()) {
            out.append(",");
        }
        out.append(_source, end + 1, suffix - end);
        if (forceEOL && !isEOLIncluded()) {
            out.append("\n");
        }
    }
}
