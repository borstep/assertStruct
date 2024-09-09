package org.assertstruct.impl.factories.date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.service.NodeParser;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;
import org.assertstruct.template.TemplateParser;

import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Value
public class AnyDateParser implements NodeParser {
    List<DateTimeFormatter> formatters;
    @Getter
    String prefix;

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token, TemplateParser templateParser) {
        return new AnyDateNode(templateKey, token, formatters);
    }
}
