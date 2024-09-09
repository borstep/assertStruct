package org.assertstruct.impl.factories.date;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.service.NodeParser;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;
import org.assertstruct.template.TemplateParseException;
import org.assertstruct.template.TemplateParser;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Collections;

@AllArgsConstructor
@Value
public class DateParser implements NodeParser {
    public static final String PREFIX = "$DATE(";

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token, TemplateParser templateParser) {
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
