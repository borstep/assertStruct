package org.assertstruct.impl.factories.date;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.node.ScalarNode;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = false)
public class AnyDateNode extends ScalarNode {
    List<DateTimeFormatter> formatters;

    public AnyDateNode(TemplateKey key, ExtToken token, List<DateTimeFormatter> formatters) {
        super(key, token);
        this.formatters = formatters;
    }

    @Override
    public boolean match(Object value, Matcher context) {
        if (value == null) {
            return false;
        }
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
