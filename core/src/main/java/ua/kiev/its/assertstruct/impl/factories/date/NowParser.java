package ua.kiev.its.assertstruct.impl.factories.date;

import lombok.Value;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.service.*;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;
import ua.kiev.its.assertstruct.template.TemplateParseException;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Value
public class NowParser implements  NodeParser {
    public static final String PREFIX = "$NOW";
    public static final String PREFIX_LONG = "$NOW(";

    /**
     * Default precision in milliseconds
     */
    long defaultPrecision;
    List<DateTimeFormatter> formatters;
    boolean strictCheck;

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token) {
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
