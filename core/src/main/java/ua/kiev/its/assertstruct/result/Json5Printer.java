package ua.kiev.its.assertstruct.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ua.kiev.its.assertstruct.AssertStruct;
import ua.kiev.its.assertstruct.converter.Wrapper;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;
import ua.kiev.its.assertstruct.template.node.ArrayNode;
import ua.kiev.its.assertstruct.template.node.ObjectNode;
import ua.kiev.its.assertstruct.template.node.StringNode;
import ua.kiev.its.assertstruct.utils.Json5Encoder;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


@RequiredArgsConstructor
public class Json5Printer {
    @Getter
    private final AssertStruct config;
    @Getter
    private final StringBuilder out;

    public Json5Printer() {
        this(AssertStruct.getDefaultInstance(), new StringBuilder());
    }

    public Json5Printer(StringBuilder out) {
        this(AssertStruct.getDefaultInstance(), out);
    }

    public Json5Printer(AssertStruct config) {
        this(config, new StringBuilder());
    }

    public String print(Object value) throws IOException {
        print(value, false, false, 0, true);
        return out.toString();
    }

    private void print(Object value, boolean trailingComma, boolean trailingEOL, int indent, boolean fromNewLine) throws IOException {
        if (value instanceof Wrapper) {
            value = ((Wrapper) value).getValue();
        }
        if (value instanceof RootResult) {
            value=((RootResult) value).getDelegate();
        }
        if (value instanceof TemplateNode) {
            ((TemplateNode) value).print(out, trailingComma, trailingEOL, indent, fromNewLine);
        } else if (value instanceof Map) {
            printDict((Map) value, trailingComma, trailingEOL, indent, fromNewLine);
        } else if (value instanceof Collection<?>) {
            printList((Collection) value, trailingComma, trailingEOL, indent, fromNewLine);
        } else {
            printValue(value, trailingComma, trailingEOL, indent, fromNewLine);
        }
    }

    private void printDict(Map value, boolean trailingComma, boolean trailingEOL, int indent, boolean fromNewLine) throws IOException {
        ObjectNode matchedTo = null;
        if (value instanceof ErrorMap) {
            matchedTo = ((ErrorMap) value).getMatchedTo();
        }
        printStart(fromNewLine ? indent : 0, matchedTo, value.isEmpty() && config.isEmptyDictOnSameLine() ? "{" : "{\n");
        boolean childEOL = true;
        int childIndent = indent + config.getIndent();
        if (!value.isEmpty()) {

            Iterator<Map.Entry> it = value.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<?, ?> child = it.next();
                Object childKey = child.getKey();
                Object childValue = child.getValue();
                if (childKey instanceof TemplateKey) {
                    ExtToken childKeyToken = ((TemplateKey) childKey).getToken();
                    childIndent = childKeyToken.getIndent();
                    childKeyToken.print(out, false, false);
                } else {
                    indent(childIndent);
                    appendKey(childKey);
                    out.append(": ");
                }
                if (childValue instanceof MatchResult) {
                    TemplateNode matchedValueNode = ((MatchResult<?>) childValue).getMatchedTo();
                    if (matchedValueNode != null) {
                        childEOL = matchedValueNode.getEndToken().isEOLIncluded();
                    }
                }
                print(childValue, it.hasNext(), childEOL, childIndent, false);
            }
        }
        printEnd(trailingComma, trailingEOL, matchedTo, indent, "}");
    }

    private int appendKey(Object childKey) { //TODO optimize performance
        return Json5Encoder.encodeKey(childKey, out, config.getQuote(), config.getForceKeyQuoting());
    }

    private void printList(Collection value, boolean trailingComma, boolean trailingEOL, int indent, boolean fromNewLine) throws IOException {
        ArrayNode matchedTo = null;
        if (value instanceof ErrorList) {
            matchedTo = ((ErrorList) value).getMatchedTo();
        }
        printStart(fromNewLine ? indent : 0, matchedTo, value.isEmpty() && config.isEmptyListOnSameLine() ? "[" : "[\n");
        boolean childEOL = true;
        int childIndent = indent + config.getIndent();
        if (!value.isEmpty()) {
            Iterator it = value.iterator();
            while (it.hasNext()) {
                Object childValue = it.next();
                if (childValue instanceof MatchResult) {
                    TemplateNode matchedValueNode = ((MatchResult) childValue).getMatchedTo();
                    if (matchedValueNode != null) {
                        childEOL = matchedValueNode.getEndToken().isEOLIncluded();
                        childIndent = matchedValueNode.getEndToken().getIndent();
                    }
                }
                print(childValue, it.hasNext(), childEOL, childIndent, true);
            }
        }
        printEnd(trailingComma, trailingEOL, matchedTo, indent, "]");
    }

    private void printValue(Object value, boolean trailingComma, boolean trailingEOL, int indent, boolean fromNewLine) throws IOException {
        TemplateNode matchedTo = null;
        if (value instanceof ErrorValue) {
            matchedTo = ((ErrorValue) value).getMatchedTo();
            value = ((ErrorValue) value).getSource();
        }
        printStart(fromNewLine ? indent : 0, matchedTo, null);
        if (value instanceof String) {
            appendStringValue((String) value, matchedTo);
        } else if (value instanceof Boolean || value instanceof Number || value == null) {
            out.append(value);
        } else if (value instanceof Collection<?> || value instanceof Map) {
            print(value, false, false, indent, false);
        } else {
            appendStringValue(value.toString(), matchedTo);
        }
        printEnd(trailingComma, trailingEOL, matchedTo, indent, null);
    }

    private void appendStringValue(String value, TemplateNode matchedTo) { //TODO optimize performance avoiding string manipulation
        char quote = config.getQuote();
        boolean multiline = false; // TODO implement multiline
        if (matchedTo instanceof StringNode) {
            quote = ((StringNode) matchedTo).getQuoteChar();
            multiline = ((StringNode) matchedTo).isMultiline();
        }
        out.append(quote);
        Json5Encoder.appendQuoted(out, value, quote);
        out.append(quote);
    }

    private void printStart(int indent, TemplateNode matchedTo, String startValue) throws IOException {
        if (matchedTo != null) {
            matchedTo.printStart(out);
        } else {
            indent(indent);
            if (startValue != null) {
                out.append(startValue);
            }
        }
    }

    private void printEnd(boolean trailingComma, boolean trailingEOL, TemplateNode matchedTo, int indent, String endValue) throws IOException {
        if (matchedTo != null) {
            matchedTo.printEnd(out, trailingComma, trailingEOL);
        } else {
            if (endValue != null) {
                indent(indent);
                out.append(endValue);
            }
            if (trailingComma) {
                out.append(',');
            }
            if (trailingEOL) {
                out.append('\n');
            }
        }
    }

    private void indent(int indent) throws IOException {
        while (indent-- > 0)
            out.append(' ');
    }

    @Override
    public String toString() {
        return out.toString();
    }
}
