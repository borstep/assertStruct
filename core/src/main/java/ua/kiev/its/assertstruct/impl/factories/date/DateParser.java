package ua.kiev.its.assertstruct.impl.factories.date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.service.NodeParser;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;
import ua.kiev.its.assertstruct.template.TemplateParseException;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Value
public class DateParser implements NodeParser {
    public static final String PREFIX = "$DATE(";

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token) {
        if (value.startsWith(PREFIX) && value.endsWith(")")) {
            String format = value.substring(PREFIX.length(), value.length() - 1).trim();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withResolverStyle(ResolverStyle.STRICT);
                return new AnyDateNode(templateKey, token, Collections.singletonList(formatter));
            } catch (Exception e) {
                throw new TemplateParseException("Unrecognized date format: " + format, e);
            }
        }
        return null;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
