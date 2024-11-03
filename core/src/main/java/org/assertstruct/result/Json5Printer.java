package org.assertstruct.result;

import lombok.Getter;
import org.assertstruct.AssertStruct;
import org.assertstruct.converter.Wrapper;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.service.AssertStructService;
import org.assertstruct.service.Config;
import org.assertstruct.template.StructTemplateNode;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;
import org.assertstruct.template.node.ArrayNode;
import org.assertstruct.template.node.ObjectNode;
import org.assertstruct.template.node.StringNode;
import org.assertstruct.utils.Json5Encoder;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static org.assertstruct.utils.ConversionUtils.*;


@Getter
public class Json5Printer {
    private final AssertStructService env;
    private final Config config;
    private final StringBuilder out;

    public Json5Printer() {
        this(AssertStruct.getDefault(), new StringBuilder());
    }

    public Json5Printer(StringBuilder out) {
        this(AssertStruct.getDefault(), out);
    }

    public Json5Printer(AssertStructService config) {
        this(config, new StringBuilder());
    }

    public Json5Printer(AssertStructService env, StringBuilder out) {
        this.env = env;
        this.out = out;
        this.config = env.getConfig();
    }

    public String print(Object value) {
        print(value, rootOpts());
        return out.toString();
    }

    private PrintOptions rootOpts() {
        PrintOptions opts = new PrintOptions();
        opts.first = true;
        opts.last = true;
        opts.fromNewLine = true;
        opts.trailingEOL = false;
        opts.indent = 0;
        opts.trailingComma = config.isTrailingComa();
        opts.firstInlineIndent = config.getFirstInlineElementIndent();
        opts.inlineSeparator = config.getInlineElementsSeparator();
        return opts;
    }

    private PrintOptions buildChildPrintOptions(PrintOptions pOpt, StructTemplateNode matchedTo, boolean isDict) {
        PrintOptions opt = new PrintOptions();
        opt.first = true;
        opt.fromNewLine = !config.isInlineContainers();
        opt.trailingEOL = !config.isInlineContainers();
        opt.indent = pOpt.indent + config.getIndent();
        opt.trailingComma = config.isTrailingComa();
        opt.firstInlineIndent = config.getFirstInlineElementIndent();
        opt.inlineSeparator = config.getInlineElementsSeparator();
        opt.field = isDict;
        if (matchedTo != null) {
            opt.fromNewLine = !matchedTo.isInline();
            opt.trailingEOL = !matchedTo.isInline();
            if (matchedTo.getFirstInlineElementIndent() != null) {
                opt.firstInlineIndent = matchedTo.getFirstInlineElementIndent();
            }
            if (matchedTo.getInlineElementsSeparator() != null) {
                opt.inlineSeparator = matchedTo.getInlineElementsSeparator();
            }
            if (matchedTo.getDefaultIndent() != null) {
                opt.indent = matchedTo.getDefaultIndent();
            }
            if (matchedTo.getTrailingComa() != null) {
                opt.trailingComma = matchedTo.getTrailingComa();
            }
        }
        return opt;
    }

    private void print(Object value, PrintOptions opts) {
        if (value instanceof Wrapper) {
            value = ((Wrapper) value).getValue();
        }
        if (value instanceof RootResult) {
            value = ((RootResult) value).getDelegate();
        }
        if (value instanceof ErrorValue) {
            ErrorValue errorValue = (ErrorValue) value;
            if (errorValue.getSource() instanceof Map || errorValue.getSource() instanceof Collection) { // if Map or collection matched by single match
                value = errorValue.getSource(); // TODO think what to do with errorValue.getMatchedTo() in this case;
            }
        }
        if (value instanceof TemplateNode) {
            printTemplateNode((TemplateNode) value, opts);
        } else if (value instanceof Map) {
            printDict((Map) value, opts);
        } else if (value instanceof Collection<?>) {
            printList((Collection) value, opts);
        } else {
            printValue(value, opts);
        }
    }

    private void printTemplateNode(TemplateNode value, PrintOptions opts) {
        ExtToken startToken = value.getStartToken();
        ExtToken endToken = value.getEndToken();
        if (startToken != endToken) {
            startToken.print(out, false, false);
            out.append(startToken.get_source(), startToken.getSuffix() + 1, endToken.getPrefix() - startToken.getSuffix() - 1);
            endToken.print(out, opts.forceComa(), opts.trailingEOL);
        } else {
            endToken.print(out, opts.forceComa(), opts.trailingEOL);
        }
    }


    //    private void printDict(Map<?,?> value, boolean trailingComma, boolean trailingEOL, int indent, boolean inline) {
    private void printDict(Map<?, ?> value, PrintOptions opts) {
        ObjectNode matchedTo = null;
        if (value instanceof ErrorMap) {
            matchedTo = ((ErrorMap) value).getMatchedTo();
        }
        printStart(opts, matchedTo, value.isEmpty() && config.isEmptyDictOnSameLine() ? "{" : config.isInlineContainers() ? "{" : "{\n");
        if (!value.isEmpty()) {
            PrintOptions cOpts = buildChildPrintOptions(opts, matchedTo, true);
            Iterator<? extends Map.Entry<?, ?>> it = value.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<?, ?> child = it.next();
                cOpts.last = !it.hasNext();
                Object childKey = child.getKey();
                Object childValue = child.getValue();
                printFieldName(cOpts, childKey);
                print(childValue, cOpts);
                calcNextPrintOpts(cOpts, childKey, childValue);
            }
        }
        printEnd(opts, matchedTo, "}");
    }

    private int appendKey(Object childKey) { //TODO optimize performance
        return Json5Encoder.encodeKey(childKey, out, config.getQuote(), config.isForceKeyQuoting());
    }

    //    private void printList(Collection value, boolean trailingComma, boolean trailingEOL, int indent, boolean inline) {
    private void printList(Collection value, PrintOptions opts) {
        ArrayNode matchedTo = null;
        if (value instanceof ErrorList) {
            matchedTo = ((ErrorList) value).getMatchedTo();
        }
        if (matchedTo == null && config.isEmptyListOnSameLine() && value.isEmpty()) {
            printStart(opts, null, null);
            out.append("[]");
            printEnd(opts, null, null);
        } else {
            printStart(opts, matchedTo, config.isInlineContainers() ? "[" : "[\n");
            if (!value.isEmpty()) {
                PrintOptions cOpts = buildChildPrintOptions(opts, matchedTo, false);
                Iterator<?> it = value.iterator();
                while (it.hasNext()) {
                    Object childValue = it.next();
                    cOpts.last = !it.hasNext();
                    print(childValue, cOpts);
                    calcNextPrintOpts(cOpts, null, childValue);
                }
            }
            printEnd(opts, matchedTo, "]");
        }
    }

    private void calcNextPrintOpts(PrintOptions cOpts, Object childKey, Object childValue) {
        ExtToken startToken = childKey instanceof TemplateKey ? ((TemplateKey) childKey).getToken() : childValue instanceof TemplateNode ? ((TemplateNode) childValue).getVeryStartToken() : null;
        if (startToken != null) {
            if (startToken.isFromNewLine()) {
                cOpts.indent = startToken.getIndent();
            } else {
                if (!cOpts.first) // skip inlineSeparator calculation for first element
                    cOpts.inlineSeparator = startToken.getLeadingSpaces();
            }

        }
        cOpts.fromNewLine = cOpts.trailingEOL; // if previous element has EOL
        if (childValue instanceof MatchResult && ((MatchResult) childValue).getMatchedTo() != null) {
            TemplateNode matchedValueNode = ((MatchResult) childValue).getMatchedTo();
            cOpts.trailingEOL = matchedValueNode.getEndToken().isEOLIncluded();
        }
        cOpts.first = false;
    }

    private void printValue(Object value, PrintOptions opts) {
        TemplateNode matchedTo = null;
        if (value instanceof ErrorValue) {
            matchedTo = ((ErrorValue) value).getMatchedTo();
            value = ((ErrorValue) value).getSource();
        }
        printStart(opts, matchedTo, null);
        if (!isJsonType(value)) {
            value = Wrapper.unwrap(env.getJsonConverter().pojo2json(value));
        }
        if (value instanceof String) {
            appendStringValue((String) value, matchedTo);
        } else if (value instanceof Boolean || value instanceof Number || value == null) {
            out.append(value);
        } else if (value instanceof Collection<?> || value instanceof Map || value instanceof Wrapper) {
            throw new IllegalArgumentException("Print value must be used for simple types only. Wrong type: " + value.getClass().getName());
        } else {
            appendStringValue(value.toString(), matchedTo);
        }
        printEnd(opts, matchedTo, null);
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

    private void printFieldName(PrintOptions opts, Object childKey) {
        if (childKey instanceof TemplateKey) {
            ExtToken childKeyToken = ((TemplateKey) childKey).getToken();
            childKeyToken.print(out, false, false);
        } else {
            if (opts.fromNewLine) {
                indent(opts.indent);
            } else {
                indent(opts.first ? opts.firstInlineIndent : opts.inlineSeparator);
            }
            appendKey(childKey);
            out.append(": ");
        }
    }

    private void printStart(PrintOptions opts, TemplateNode matchedTo, String startValue) {
        if (matchedTo != null) {
            matchedTo.printStart(out);
        } else {
            if (!opts.field) {
                if (opts.fromNewLine) {
                    indent(opts.indent);
                } else {
                    indent(opts.first ? opts.firstInlineIndent : opts.inlineSeparator);
                }
            }
            if (startValue != null) {
                out.append(startValue);
            }
        }
    }

    private void printEnd(PrintOptions opts, TemplateNode matchedTo, String endValue) {
        if (matchedTo != null) {
            matchedTo.printEnd(out, opts.forceComa(), opts.trailingEOL);
        } else {
            if (endValue != null) {
                if (opts.fromNewLine) {
                    indent(opts.indent);
                }
                out.append(endValue);
            }
            if (opts.forceComa()) {
                out.append(',');
            }
            if (opts.trailingEOL) {
                out.append('\n');
            }
        }
    }

    private void indent(int indent) {
        while (indent-- > 0)
            out.append(' ');
    }

    @Override
    public String toString() {
        return out.toString();
    }

    static class PrintOptions {
        // Container level
        /**
         * print comma after last element
         */
        boolean trailingComma;
        /**
         * Container is object, print field value
         */
        boolean field;
        /**
         * number of spaces to indent as first element in container without new line
         */
        public int firstInlineIndent;

        // Calculated based in position
        /**
         * First element in container
         */
        public boolean first;
        /**
         * last element in container
         */
        public boolean last;
        // Calculated based in previous element
        /**
         * previous element ended with new line
         */
        public boolean fromNewLine;
        /**
         * number of spaces to indent between elements in container without new line
         */
        public int inlineSeparator;
        /**
         * Print new line at the end
         */
        boolean trailingEOL;
        /**
         * number of space to indent if printing on new line
         */
        int indent;

        boolean forceComa() {
            return trailingComma || !last;
        }
    }
}
