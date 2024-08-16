package ua.kiev.its.assertstruct.impl.factories.date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.service.NodeParser;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;

import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Value
public class AnyDateParser implements NodeParser {
    List<DateTimeFormatter> formatters;
    @Getter
    String prefix;

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token) {
        return new AnyDateNode(templateKey, token, formatters);
    }
}
