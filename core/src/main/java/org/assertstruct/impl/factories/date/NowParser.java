package org.assertstruct.impl.factories.date;

import lombok.Value;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.service.NodeParser;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;
import org.assertstruct.template.TemplateParseException;
import org.assertstruct.template.TemplateParser;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Value
public class NowParser implements NodeParser {
    public static final String PREFIX = "$NOW";
    public static final String PREFIX_LONG = "$NOW(";

    /**
     * Default precision in milliseconds
     */
    long defaultPrecision;
    List<DateTimeFormatter> formatters;
    boolean strictCheck;

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token, TemplateParser templateParser) {
        if (value.equals(PREFIX)) {
            return new NowNode( defaultPrecision, strictCheck, formatters, templateKey, token);
        } else if (value.startsWith(PREFIX_LONG)&&value.endsWith(")") ) {
            try {
                long precision = Long.parseLong(value.substring(PREFIX_LONG.length(), value.length()-1).trim());
                return new NowNode( precision, strictCheck, formatters, templateKey, token);
            } catch (NumberFormatException e) {
                throw new TemplateParseException("$NOW precision must be a number in seconds");
            }
        }
        return null;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

}
