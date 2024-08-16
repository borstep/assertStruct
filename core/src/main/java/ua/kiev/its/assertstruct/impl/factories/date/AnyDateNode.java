package ua.kiev.its.assertstruct.impl.factories.date;

import lombok.Value;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.matcher.Matcher;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.node.ScalarNode;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Value
public class AnyDateNode extends ScalarNode {
    List<DateTimeFormatter> formatters;

    public AnyDateNode(TemplateKey key, ExtToken token, List<DateTimeFormatter> formatters) {
        super(key, token);
        this.formatters = formatters;
    }

    @Override
    public boolean match(Object value, Matcher context) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                formatter.parse(value.toString());
                return true;
            } catch (Exception ignore) {
            }
        }
        return false;
    }

}
